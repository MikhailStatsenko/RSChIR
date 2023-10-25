package main

import (
	"PW_8/internal/cookiehandler"
	"PW_8/web"
	"github.com/joho/godotenv"
	"github.com/sirupsen/logrus"
	"io"
	"os"
)

var log = logrus.New()

func init() {

	log.SetFormatter(&logrus.TextFormatter{})

	file, err := os.OpenFile("logs.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err == nil {
		log.SetOutput(io.MultiWriter(os.Stdout, file))
	} else {
		log.Info("Failed to log to file, using default stderr")
	}
}

func main() {
	err := godotenv.Load("./.env")
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	port := os.Getenv("PORT")
	cookieName := os.Getenv("COOKIE_NAME")

	cookieSecret := []byte("zZ3EVq4omDKWBMCq9yiV0TMZPjLaKKqn")

	cookieHandler, err := cookiehandler.NewCookieHandler(cookieSecret)
	if err != nil {
		log.Fatalf("Error creating CookieHandler: %v", err)
	}

	server := web.NewServer(port, cookieName, cookieSecret, *cookieHandler, log)
	err = server.Start()
	if err != nil {
		log.Fatalf("Error starting server: %v", err)
	}
}
