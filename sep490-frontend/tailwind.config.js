/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  darkMode: ['selector', '[class~="my-app-dark"]'],
  theme: {
    extend: {
      colors: {
        green: {
          50: '#fafcfa',
          100: '#e5f2e6',
          200: '#d0e7d2',
          300: '#bbddbe',
          400: '#a6d2aa',
          500: '#91c896',
          600: '#7baa80',
          700: '#668c69',
          800: '#506e53',
          900: '#3a503c',
          950: '#243226'
        },
        red: {
          50: '#fef8f8',
          100: '#fbdfdf',
          200: '#f8c5c6',
          300: '#f5acad',
          400: '#f29293',
          500: '#ef797a',
          600: '#cb6768',
          700: '#a75555',
          800: '#834343',
          900: '#603031',
          950: '#3c1e1f'
        },
        yellow: {
          50: '#fffdf3',
          100: '#fff5c3',
          200: '#ffed94',
          300: '#ffe464',
          400: '#ffdc35',
          500: '#ffd405',
          600: '#d9b404',
          700: '#b39404',
          800: '#8c7503',
          900: '#665502',
          950: '#403501'
        },
        sky: {
          50: '#f9fdfd',
          100: '#e4f4f7',
          200: '#cfebf0',
          300: '#bae2e9',
          400: '#a5dae3',
          500: '#90d1dc',
          600: '#7ab2bb',
          700: '#65929a',
          800: '#4f7379',
          900: '#3a5458',
          950: '#243437'
        },
        black: '#2A2A2A'
      },
      fontFamily: {
        sans: ['HeraclitoRegular', 'sans-serif']
      }
    }
  },
  plugins: [require('tailwindcss-primeui')]
};
