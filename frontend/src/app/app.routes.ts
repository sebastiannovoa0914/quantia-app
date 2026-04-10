import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';

// Importaciones corregidas apuntando a las carpetas exactas de tu VS Code
import { CrearPropiedadComponent } from './components/crear-propiedad/crear-propiedad'; 
import { RegistroComponent } from './components/registro/registro';
import { ExplorarComponent } from './components/explorar/explorar'; 

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'home', component: HomeComponent },
  { path: 'crear-propiedad', component: CrearPropiedadComponent },
  { path: 'explorar', component: ExplorarComponent }, 
  
  // Redirección por defecto
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];