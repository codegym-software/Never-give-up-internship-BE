# Stage 1: Build
FROM node:22-alpine AS build

# Nhận các biến môi trường build-time từ docker-compose
ARG VITE_API_URL
ARG VITE_SOCKET_URL
ARG VITE_REFESH_TOKEN_URL
ARG VITE_GOOGLE_CLIENT_ID

# Thiết lập các biến môi trường này cho các lệnh tiếp theo (quan trọng cho npm build)
ENV VITE_API_URL=$VITE_API_URL
ENV VITE_SOCKET_URL=$VITE_SOCKET_URL
ENV VITE_REFESH_TOKEN_URL=$VITE_REFESH_TOKEN_URL
ENV VITE_GOOGLE_CLIENT_ID=$VITE_GOOGLE_CLIENT_ID

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
# Nginx will start automatically