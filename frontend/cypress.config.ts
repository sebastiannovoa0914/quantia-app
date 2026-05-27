import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    specPattern: 'cypress/e2e/**/*.cy.{js,jsx,ts,tsx}', // <--- AGREGA ESTA LÍNEA
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
    baseUrl: 'http://localhost:80', // <--- OPCIONAL: Para no escribir la URL siempre
  },
});