import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // MÉTODO DE LOGIN (Ya lo tenías, mantenemos la lógica del token)
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap((res: any) => { 
        console.log('Respuesta cruda del servidor:', res);
        if (res && res.token) {
          localStorage.setItem('token', res.token);
          localStorage.setItem('nombre', res.nombre || ''); 
          localStorage.setItem('rol', res.rol || '');
          console.log('Datos guardados en LocalStorage');
        }
      })
    );
  }

  // MÉTODO DE REGISTRO (Nuevo: Basado en tu captura de Postman)
  register(userData: any): Observable<any> {
    // Estructuramos el objeto para que coincida exactamente con tu tabla de BD
    const body = {
      nombre: userData.nombre,
      email: userData.email,
      contrasena: userData.contrasena,
      rol: 'ADMINISTRADOR', // Valor por defecto según tu Postman
      activo: true        // Valor por defecto según tu Postman
    };
    return this.http.post(`${this.apiUrl}/auth/register`, body);
  }

  // OBTENER PERFIL
  getProfile(): Observable<any> {
    return this.http.get(`${this.apiUrl}/home/perfil`);
  }

  // CERRAR SESIÓN
  logout() { 
    localStorage.clear(); 
    console.log('Sesión cerrada y storage limpio');
  }
}