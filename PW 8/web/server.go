package web

import (
	"PW_8/internal/cookiehandler"
	"encoding/json"
	"fmt"
	"github.com/sirupsen/logrus"
	"io"
	"net/http"
	"time"
)

type Server struct {
	Port          string
	CookieName    string
	CookieSecret  []byte
	CookieHandler cookiehandler.CookieHandler
	log           *logrus.Logger
}

func NewServer(port, cookieName string, cookieSecret []byte, cookieHandler cookiehandler.CookieHandler, log *logrus.Logger) *Server {
	return &Server{
		Port:          port,
		CookieName:    cookieName,
		CookieSecret:  cookieSecret,
		CookieHandler: cookieHandler,
		log:           log,
	}
}

func (s *Server) Start() error {
	http.HandleFunc("/api/linear", s.LinearHandler)
	http.HandleFunc("/api/concurrent", s.ConcurrentHandler)
	http.HandleFunc("/api/cookies-decoded", s.ViewCookiesDecoded)
	http.HandleFunc("/api/cookies", s.ViewCookies)
	return http.ListenAndServe(":"+s.Port, nil)
}

func (s *Server) LinearHandler(w http.ResponseWriter, r *http.Request) {
	body, err := io.ReadAll(r.Body)
	if err != nil {
		s.log.Errorf("Error reading request body: %v", err)
		http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		return
	}

	var requestData struct {
		Name string `json:"name"`
		Age  int    `json:"age"`
	}

	if err := json.Unmarshal(body, &requestData); err != nil {
		s.log.Errorf("Error decoding JSON: %v", err)
		http.Error(w, "Bad Request", http.StatusBadRequest)
		return
	}

	jsonData, err := json.Marshal(requestData)
	if err != nil {
		s.log.Errorf("Error marshalling data: %v", err)
		http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		return
	}

	encryptedCookie, err := s.CookieHandler.EncodeAndSign(jsonData)
	if err != nil {
		s.log.Errorf("Error encoding and signing cookie: %v", err)
		http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		return
	}

	http.SetCookie(w, &http.Cookie{
		Name:  s.CookieName,
		Value: encryptedCookie,
	})
	time.Sleep(2 * time.Second)

	w.WriteHeader(http.StatusOK)
	fmt.Fprintln(w, "Linear Handler: saved cookie\n"+s.CookieName+": "+encryptedCookie)

	s.log.Info("Linear Handler: saved cookie")
}

func (s *Server) ConcurrentHandler(w http.ResponseWriter, r *http.Request) {
	body, err := io.ReadAll(r.Body)
	if err != nil {
		s.log.Errorf("Error reading request body: %v", err)
		http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		return
	}

	var requestData struct {
		Name string `json:"name"`
		Age  int    `json:"age"`
	}

	if err := json.Unmarshal(body, &requestData); err != nil {
		s.log.Errorf("Error decoding JSON: %v", err)
		http.Error(w, "Bad Request", http.StatusBadRequest)
		return
	}

	jsonData, err := json.Marshal(requestData)
	if err != nil {
		s.log.Errorf("Error marshalling data: %v", err)
		http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		return
	}

	encryptedCookie, err := s.CookieHandler.EncodeAndSign(jsonData)

	go func() {
		time.Sleep(2 * time.Second)
	}()

	http.SetCookie(w, &http.Cookie{
		Name:  s.CookieName,
		Value: encryptedCookie,
	})

	w.WriteHeader(http.StatusOK)
	fmt.Fprintln(w, "Concurrent Handler: saved cookie\n"+s.CookieName+": "+encryptedCookie)

	s.log.Info("Concurrent Handler: saved cookie")
}

func (s *Server) ViewCookiesDecoded(w http.ResponseWriter, r *http.Request) {
	cookies := r.Cookies()
	cookieData := make([]map[string]interface{}, 0)

	for _, cookie := range cookies {
		decodedValue, err := s.CookieHandler.DecodeAndVerify(cookie.Value)
		cookieInfo := make(map[string]interface{})

		if err != nil {
			cookieInfo["cookie-name"] = cookie.Name
			cookieInfo["error"] = err.Error()
		} else {
			jsonValue, ok := decodedValue.([]byte)
			if !ok {
				cookieInfo["cookie-name"] = cookie.Name
				cookieInfo["error"] = "Unexpected decoded value type"
			} else {
				var data struct {
					Name string `json:"name"`
					Age  int    `json:"age"`
				}
				err := json.Unmarshal(jsonValue, &data)
				if err != nil {
					cookieInfo["cookie-name"] = cookie.Name
					cookieInfo["error"] = "Error decoding JSON"
				} else {
					cookieInfo["cookie-name"] = cookie.Name
					cookieInfo["name"] = data.Name
					cookieInfo["age"] = data.Age
				}
			}
		}

		cookieData = append(cookieData, cookieInfo)
	}

	responseJSON, err := json.Marshal(cookieData)
	if err != nil {
		http.Error(w, "Error encoding response to JSON", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write(responseJSON)
}

func (s *Server) ViewCookies(w http.ResponseWriter, r *http.Request) {
	cookies := r.Cookies()

	type cookieData struct {
		Name  string `json:"name"`
		Value string `json:"value"`
	}

	var cookieDataList []cookieData

	for _, cookie := range cookies {
		cookieDataList = append(cookieDataList, cookieData{Name: cookie.Name, Value: cookie.Value})
	}

	jsonResponse, err := json.Marshal(cookieDataList)
	if err != nil {
		http.Error(w, "Failed to marshal JSON", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write(jsonResponse)
}
