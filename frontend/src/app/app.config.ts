import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { authInterceptor } from './services/auth.interceptor'; 

export const appConfig: ApplicationConfig = {
  providers: [
    // Configura las rutas de la aplicación (Home, Login, Crear Propiedad, etc.)
    provideRouter(routes),

    // Configura el cliente HTTP para que use el Interceptor de seguridad
    provideHttpClient(
      withInterceptors([authInterceptor])
    )
  ]
};