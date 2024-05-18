module.exports = {
  content: ["./src/**/*.ftl", "./resources/templates/*.ftl", "./resources/templates/**/*.ftl"],
  theme: {
    extend: {
      colors: {
        primary: '#FF6363',
        secondary: {
          100: '#E2E2D5',
          200: '#888883',
        }
      }
    },
  },
  variants: {},
  plugins: [],
}
