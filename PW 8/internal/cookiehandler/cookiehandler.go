package cookiehandler

import (
	"crypto/aes"
	"crypto/cipher"
	"crypto/hmac"
	"crypto/rand"
	"crypto/sha256"
	"encoding/base64"
	"encoding/json"
	"errors"
)

type CookieHandler struct {
	secretKey []byte
	block     cipher.Block
}

func NewCookieHandler(secretKey []byte) (*CookieHandler, error) {
	if len(secretKey) != 32 {
		return nil, errors.New("invalid secret key length, should be 32 bytes")
	}
	block, err := aes.NewCipher(secretKey)
	if err != nil {
		return nil, err
	}
	return &CookieHandler{secretKey: secretKey, block: block}, nil
}

func (c *CookieHandler) EncodeAndSign(data interface{}) (string, error) {
	jsonData, err := json.Marshal(data)
	if err != nil {
		return "", err
	}

	gcm, err := cipher.NewGCM(c.block)
	if err != nil {
		return "", err
	}
	nonce := make([]byte, gcm.NonceSize())
	if _, err := rand.Read(nonce); err != nil {
		return "", err
	}
	encryptedData := gcm.Seal(nonce, nonce, jsonData, nil)

	hmacHash := hmac.New(sha256.New, c.secretKey)
	hmacHash.Write(encryptedData)
	signature := hmacHash.Sum(nil)

	cookieValue := append(encryptedData, signature...)
	encodedCookie := base64.StdEncoding.EncodeToString(cookieValue)
	return encodedCookie, nil
}

func (c *CookieHandler) DecodeAndVerify(cookieValue string) (interface{}, error) {
	decodedCookie, err := base64.StdEncoding.DecodeString(cookieValue)
	if err != nil {
		return nil, err
	}

	dataSize := len(decodedCookie) - sha256.Size
	data, mac := decodedCookie[:dataSize], decodedCookie[dataSize:]
	hmacHash := hmac.New(sha256.New, c.secretKey)
	hmacHash.Write(data)
	if !hmac.Equal(hmacHash.Sum(nil), mac) {
		return nil, errors.New("cookie signature mismatch")
	}

	gcm, err := cipher.NewGCM(c.block)
	if err != nil {
		return nil, err
	}
	nonceSize := gcm.NonceSize()
	if dataSize < nonceSize {
		return nil, errors.New("invalid cookie size")
	}
	nonce, encryptedData := data[:nonceSize], data[nonceSize:]
	decryptedData, err := gcm.Open(nil, nonce, encryptedData, nil)
	if err != nil {
		return nil, err
	}

	var result []byte
	if err := json.Unmarshal(decryptedData, &result); err != nil {
		return nil, err
	}

	return result, nil
}
