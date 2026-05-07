import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  // Asegúrate de que esta URL sea accesible desde el navegador donde corres el frontend
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> {
    // El backend espera 'email' y 'contrasena' según tu AuthController
    return this.http.post(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap((res: any) => { 
        if (res && res.token) {
          localStorage.setItem('token', res.token);
          localStorage.setItem('nombre', res.nombre || ''); 
          localStorage.setItem('rol', res.rol || '');
          // Guardamos el ID para usarlo en la creación de proyectos
          localStorage.setItem('id_usuario', res.id); 
          
          console.log('Login exitoso. ID de usuario guardado:', res.id);
        }
      })
    );
  }

  register(userData: any): Observable<any> {
    // Mapeo explícito para asegurar que los nombres de los campos
    // coincidan con los atributos de la entidad Usuario.java en el backend
    const body = {
      nombre: userData.nombre,
      email: userData.email,
      contrasena: userData.contrasena, // Verifica que en Java sea 'contrasena'
      rol: 'ADMINISTRADOR', 
      activo: true
    };
    return this.http.post(`${this.apiUrl}/auth/register`, body);
  }

  getProfile(): Observable<any> {
    // Este endpoint requiere que el JwtAuthenticationFilter esté funcionando
    return this.http.get(`${this.apiUrl}/home/perfil`);
  }

  logout() { 
    localStorage.clear(); 
    console.log('Sesión cerrada. LocalStorage limpio.');
  }

  // Método de utilidad para saber si el usuario está logueado
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}