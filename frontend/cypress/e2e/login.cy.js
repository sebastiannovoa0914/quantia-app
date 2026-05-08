describe('Flujo de Login - Quantia Portal', () => {
  it('Debe iniciar sesión con un usuario válido', () => {
    cy.visit('http://localhost:4200/login');

    // Usamos las credenciales que ya confirmamos en tu MySQL
    cy.get('input[name="email"]').type('nuevo_usuario_mayo@quantia.com');
    cy.get('input[name="contrasena"]').type('clave12345');

    cy.get('button[type="submit"]').click();

    // Verificamos el cambio de URL al Home
    cy.url().should('include', '/home');

    // Corregimos la búsqueda del texto usando expresiones regulares /i
    cy.contains(/resumen proyectos/i, { timeout: 10000 }).should('be.visible');
    cy.contains(/juan sebastian novoa/i).should('be.visible');
    
    // Verificamos que la tabla de proyectos cargó datos del backend
    cy.get('table').should('contain', 'sauces');
  });
});