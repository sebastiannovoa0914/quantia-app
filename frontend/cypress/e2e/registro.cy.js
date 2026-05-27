it('Debe registrar un nuevo usuario exitosamente', () => {
  cy.visit('http://localhost:80/registro');

  // IMPORTANTE: Cambia el correo a uno nuevo cada vez que pruebes 
  // para evitar errores de "Email ya existe" en el servidor.
  cy.get('input[name="nombre"]').type('Sebastian Novoa');
  cy.get('input[name="email"]').type('nuevo_intentofinal_01@quantia.co'); 
  cy.get('input[name="contrasena"]').type('clave12');

  cy.get('button[type="submit"]').click();

  // 1. Aumentamos el tiempo de espera de la aserción (timeout)
  // Esto le da al servidor hasta 10 segundos para responder antes de fallar.
  cy.url({ timeout: 10000 }).should('include', '/login');

  // 2. Verificamos un elemento visual de la página de Login para confirmar
  cy.contains('INGRESAR AL SISTEMA', { timeout: 10000 }).should('be.visible');
});