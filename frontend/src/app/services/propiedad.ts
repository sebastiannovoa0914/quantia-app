import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs'; // Necesario para el canal de comunicación
import { tap } from 'rxjs/operators'; // Necesario para interceptar la respuesta

@Injectable({
  providedIn: 'root'
})
export class ProyectoService {
  private apiUrl = 'http://localhost:8080/api/proyectos';

  // 1. Creamos el BehaviorSubject (El canal de noticias)
  // Se inicializa en undefined porque al arrancar no hay cambios aún.
  private _refreshNeeded$ = new BehaviorSubject<void>(undefined);

  // 2. Exponemos el canal como un Observable para que el Home pueda "escuchar"
  get refreshNeeded$() {
    return this._refreshNeeded$;
  }

  constructor(private http: HttpClient) {}

  // 3. Modificamos el método crear para que "avise" al canal cuando termine
  crearProyecto(proyecto: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(this.apiUrl, proyecto, { headers }).pipe(
      tap(() => {
        // .next() envía la señal de "¡Hay datos nuevos!" a todos los suscritos
        this._refreshNeeded$.next();
      })
    );
  }

  listarProyectos(): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get(this.apiUrl, { headers });
  }

  actualizarProgreso(id: number, progreso: number): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    // Usamos PATCH para actualizar solo el campo progreso
    return this.http.patch(`${this.apiUrl}/${id}/progreso`, { progreso }, { headers });
  }
  eliminarProyecto(id: number): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  
    return this.http.delete(`${this.apiUrl}/${id}`, { headers }).pipe(
      tap(() => this._refreshNeeded$.next()) // Avisa que algo cambió
    );
  }
  obtenerProyectoPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
  actualizarProyecto(id: number, proyecto: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, proyecto).pipe(
      tap(() => this._refreshNeeded$.next())
    );
  }
}