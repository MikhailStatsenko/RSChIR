FROM golang:1.21 AS build

WORKDIR /app

COPY go.mod .
COPY go.sum .

RUN go mod download

COPY . .

RUN CGO_ENABLED=0 GOOS=linux go build -o app ./cmd/cookie-app

FROM scratch

COPY --from=build /app/app /app

EXPOSE 8081

CMD ["/app"]

