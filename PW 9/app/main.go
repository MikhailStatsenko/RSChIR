package main

import (
	"context"
	"encoding/json"
	"github.com/gorilla/mux"
	"github.com/joho/godotenv"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/gridfs"
	"go.mongodb.org/mongo-driver/mongo/options"
	"io"
	"log"
	"net/http"
	"os"
)

func connectToMongoDB() (*mongo.Client, error) {
	clientOptions := options.Client().ApplyURI(os.Getenv("MONGO_URL"))
	client, err := mongo.Connect(context.Background(), clientOptions)
	if err != nil {
		return nil, err
	}
	return client, nil
}

func readGridFSFile(fs *gridfs.Bucket, fileID interface{}) (string, error) {
	downloadStream, err := fs.OpenDownloadStream(fileID)
	if err != nil {
		return "", err
	}
	defer downloadStream.Close()

	data, err := io.ReadAll(downloadStream)
	if err != nil {
		return "", err
	}
	return string(data), nil
}

func getAllFilesHandler(w http.ResponseWriter, r *http.Request) {
	client, err := connectToMongoDB()
	if err != nil {
		http.Error(w, "Failed to connect to MongoDB", http.StatusInternalServerError)
		return
	}
	defer client.Disconnect(context.Background())

	db := client.Database("db")
	filesCollection := db.Collection("fs.files")
	filter := bson.D{}

	cursor, err := filesCollection.Find(context.Background(), filter)
	if err != nil {
		log.Fatal(err)
	}
	defer cursor.Close(context.Background())

	var filesInfo []map[string]interface{}
	for cursor.Next(context.Background()) {
		var fileInfo bson.M
		if err := cursor.Decode(&fileInfo); err != nil {
			log.Fatal(err)
		}

		fileInfoMap := map[string]interface{}{
			"fileID": fileInfo["_id"],
		}
		filesInfo = append(filesInfo, fileInfoMap)
	}

	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "application/json")
	jsonData, err := json.MarshalIndent(filesInfo, "", "  ")
	if err != nil {
		http.Error(w, "Failed to marshal JSON", http.StatusInternalServerError)
		return
	}
	w.Write(jsonData)
}

func getFileInfoHandler(w http.ResponseWriter, r *http.Request) {
	client, err := connectToMongoDB()
	if err != nil {
		http.Error(w, "Failed to connect to MongoDB", http.StatusInternalServerError)
		return
	}
	defer client.Disconnect(context.Background())

	vars := mux.Vars(r)
	fileID, err := primitive.ObjectIDFromHex(vars["id"])
	if err != nil {
		http.Error(w, "Failed to connect to MongoDB", http.StatusInternalServerError)
		return
	}

	db := client.Database("db")

	filesCollection := db.Collection("fs.files")

	filter := bson.M{"_id": fileID}

	var fileInfo bson.M
	err = filesCollection.FindOne(context.Background(), filter).Decode(&fileInfo)
	if err != nil {
		http.Error(w, "File not found", http.StatusNotFound)
		return
	}

	fileInfoMap := map[string]interface{}{
		"fileID":   fileInfo["_id"],
		"filename": fileInfo["filename"],
		"length":   fileInfo["length"],
	}
	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "application/json")
	jsonData, err := json.MarshalIndent(fileInfoMap, "", "  ")
	if err != nil {
		http.Error(w, "Failed to marshal JSON", http.StatusInternalServerError)
		return
	}
	w.Write(jsonData)
}

func uploadFileHandler(w http.ResponseWriter, r *http.Request) {
	file, handler, err := r.FormFile("file")
	if err != nil {
		http.Error(w, "Failed to get file from request", http.StatusBadRequest)
		return
	}
	defer file.Close()

	filename := handler.Filename

	client, err := connectToMongoDB()
	if err != nil {
		println(1)
		http.Error(w, "Failed to connect to MongoDB", http.StatusInternalServerError)
		return
	}
	defer client.Disconnect(context.Background())

	db := client.Database("db")

	fs, err := gridfs.NewBucket(db)
	if err != nil {
		http.Error(w, "Failed to create GridFS bucket", http.StatusInternalServerError)
		return
	}

	uploadStream, err := fs.OpenUploadStream(filename)
	if err != nil {
		http.Error(w, "Failed to open upload stream", http.StatusInternalServerError)
		return
	}
	defer uploadStream.Close()

	_, err = io.Copy(uploadStream, file)
	if err != nil {
		http.Error(w, "Failed to copy file to upload stream", http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("File uploaded successfully"))
}

func getFileByIdHandler(w http.ResponseWriter, r *http.Request) {
	client, err := connectToMongoDB()
	if err != nil {
		http.Error(w, "Failed to connect to MongoDB", http.StatusInternalServerError)
		return
	}
	defer client.Disconnect(context.Background())

	vars := mux.Vars(r)
	fileID, err := primitive.ObjectIDFromHex(vars["id"])
	if err != nil {
		http.Error(w, "Failed to connect to MongoDB", http.StatusInternalServerError)
		return
	}

	db := client.Database("db")
	filesCollection := db.Collection("fs.files")
	filter := bson.M{"_id": fileID}

	var fileInfo bson.M
	err = filesCollection.FindOne(context.Background(), filter).Decode(&fileInfo)
	if err != nil {
		http.Error(w, "File not found", http.StatusNotFound)
		return
	}

	fs, err := gridfs.NewBucket(
		db,
	)
	content, err := readGridFSFile(fs, fileInfo["_id"])
	if err != nil {
		log.Fatal(err)
	}

	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "application/json")
	jsonData, err := json.MarshalIndent(content, "", "  ")
	if err != nil {
		http.Error(w, "Failed to marshal JSON", http.StatusInternalServerError)
		return
	}
	w.Write(jsonData)
}

func deleteFileHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	fileID, err := primitive.ObjectIDFromHex(vars["id"])
	if err != nil {
		http.Error(w, "Invalid file ID format", http.StatusBadRequest)
		return
	}

	client, err := connectToMongoDB()
	if err != nil {
		http.Error(w, "Failed to connect to MongoDB", http.StatusInternalServerError)
		return
	}
	defer client.Disconnect(context.Background())

	db := client.Database("db")
	fs, err := gridfs.NewBucket(db)
	if err != nil {
		http.Error(w, "Failed to create GridFS bucket", http.StatusInternalServerError)
		return
	}

	err = fs.Delete(fileID)
	if err != nil {
		http.Error(w, "Failed to delete file", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	w.Write([]byte("File deleted successfully"))
}

func updateFileHandler(w http.ResponseWriter, r *http.Request) {
	file, handler, err := r.FormFile("file")
	if err != nil {
		http.Error(w, "Failed to get file from request", http.StatusBadRequest)
		return
	}
	defer file.Close()

	client, err := connectToMongoDB()
	if err != nil {
		http.Error(w, "Failed to connect to MongoDB", http.StatusInternalServerError)
		return
	}
	defer client.Disconnect(context.Background())

	vars := mux.Vars(r)
	fileID, err := primitive.ObjectIDFromHex(vars["id"])
	if err != nil {
		http.Error(w, "Invalid file ID format", http.StatusBadRequest)
		return
	}

	db := client.Database("db")
	fs, err := gridfs.NewBucket(db)
	if err != nil {
		http.Error(w, "Failed to create GridFS bucket", http.StatusInternalServerError)
		return
	}
	err = fs.Delete(fileID)
	if err != nil {
		http.Error(w, "Failed to delete the old file", http.StatusInternalServerError)
		return
	}
	newFileID := fileID
	newFilename := handler.Filename
	uploadStream, err := fs.OpenUploadStreamWithID(newFileID, newFilename)
	if err != nil {
		http.Error(w, "Failed to open upload stream", http.StatusInternalServerError)
		return
	}
	defer uploadStream.Close()

	_, err = io.Copy(uploadStream, file)
	if err != nil {
		http.Error(w, "Failed to copy file to upload stream", http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("File updated successfully"))
}

func main() {
	if err := godotenv.Load(); err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}
	r := mux.NewRouter()
	r.HandleFunc("/files", getAllFilesHandler).Methods("GET")
	r.HandleFunc("/files/{id}", getFileByIdHandler).Methods("GET")
	r.HandleFunc("/files/{id}/info", getFileInfoHandler).Methods("GET")
	r.HandleFunc("/files", uploadFileHandler).Methods("POST")
	r.HandleFunc("/files/{id}", deleteFileHandler).Methods("DELETE")
	r.HandleFunc("/files/{id}", updateFileHandler).Methods("PUT")

	http.Handle("/", r)

	port := os.Getenv("PORT")
	log.Printf("Server is running on port %s\n", port)
	http.ListenAndServe(":"+port, nil)
}
