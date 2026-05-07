# 1. Etapa de compilación
FROM node:20 AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --configuration=production

# 2. Etapa de servidor (Nginx)
FROM nginx:alpine

# Copiamos los archivos compilados de Angular
COPY --from=build /app/dist/*/browser /usr/share/nginx/html/

# --- CAMBIO CLAVE AQUÍ ---
# Copiamos nuestra configuración de Nginx para habilitar el enrutamiento (SPA)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto estándar
EXPOSE 80

# Arrancamos Nginx
CMD ["nginx", "-g", "daemon off;"]