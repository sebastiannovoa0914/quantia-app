import { Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { RegistroComponent } from './components/registro/registro'; 
import { HomeComponent } from './components/home/home.component';
import { CrearPropiedad } from './components/crear-propiedad/crear-propiedad'; 
import { ContabilidadComponent } from './components/contabilidad/contabilidad.component'; // Tu nuevo componente
import { UsuariosComponent } from './components/usuarios/usuarios.component';


export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'home', component: HomeComponent },               // Tu Home intacto con toda su lógica
  { path: 'crear-propiedad', component: CrearPropiedad },
  { path: 'contabilidad', component: ContabilidadComponent }, // Agregamos la ruta limpia aquí
  { path: 'usuarios', component: UsuariosComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];