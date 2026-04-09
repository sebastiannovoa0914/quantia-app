import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
// Sin el ".component" porque tus archivos se llaman crear-propiedad.ts y registro.ts
import { CrearPropiedadComponent } from './components/crear-propiedad/crear-propiedad';
import { RegistroComponent } from './components/registro/registro';

export const routes: Routes = [
  // Ruta de Login
  { path: 'login', component: LoginComponent },
  
  // Ruta de Registro
  { path: 'registro', component: RegistroComponent },
  
  // Panel Principal
  { path: 'home', component: HomeComponent },
  
  // Gestión de Propiedades
  { path: 'crear-propiedad', component: CrearPropiedadComponent },

  // Redirecciones
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];