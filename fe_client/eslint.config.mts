import js from "@eslint/js";
import globals from "globals";
import tseslint from "typescript-eslint";
// import pluginReact from "eslint-plugin-react"; // Removed as it's not directly used anymore

// Import the FlatCompat utility
import { FlatCompat } from "@eslint/eslintrc";
import path from "path";
import { fileURLToPath } from "url";

// Try wildcard import for defineConfig
import * as eslintConfig from "eslint/config";


// mimic CommonJS variables -- not needed if using CommonJS
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const compat = new FlatCompat({
  baseDirectory: __dirname,
  resolvePluginsRelativeTo: __dirname,
});

export default eslintConfig.defineConfig([
  {
    files: ["**/*.{js,mjs,cjs,ts,mts,cts,jsx,tsx}"],
    ignores: ["build/**"], // Ignore the build directory
    extends: [
      js.configs.recommended, // Base JS recommended rules
      ...tseslint.configs.recommended, // TypeScript recommended rules
      ...compat.extends("plugin:react/recommended"), // Use compat for React recommended rules
      // compat.extends("plugin:react/jsx-runtime"), // If using React 17+ new JSX transform
    ],
    languageOptions: {
      globals: {
        ...globals.browser,
        React: true, // Temporarily add React global to suppress "React is not defined" warnings
      },
      parser: tseslint.parser, // Specify the parser for TypeScript files
      parserOptions: {
        ecmaVersion: "latest",
        sourceType: "module",
        ecmaFeatures: {
          jsx: true,
        },
      },
    },
    settings: {
      react: {
        version: "detect", // Automatically detect the React version
      },
    },
    rules: {
      // Re-apply rules to ensure they are not overridden or to customize
      "react/react-in-jsx-scope": "off", // Disable for new JSX transform
      "react/jsx-uses-react": "off", // Disable for new JSX transform
      // "react/prop-types": "off", // Moved to a separate config object below
      
      "@typescript-eslint/no-explicit-any": "warn",
      "@typescript-eslint/no-unused-vars": ["warn", { "argsIgnorePattern": "^_" }],
      "no-undef": "warn", 
      "no-prototype-builtins": "warn",
      "no-cond-assign": ["error", "always"],
      "no-empty": "warn",
      "getter-return": "error",
      "no-misleading-character-class": "error",
      "no-useless-escape": "error",
      "valid-typeof": "error",
      "react/display-name": "warn",
      "@typescript-eslint/no-this-alias": "warn",
    },
  },
  // Specific config for vite.config.ts to handle Node.js globals like __dirname
  {
    files: ["vite.config.ts"],
    languageOptions: {
      globals: {
        ...globals.node, // Add Node.js globals for vite.config.ts
      },
    },
  },
  // Explicitly turn off react/prop-types for TypeScript files
  {
    files: ["**/*.ts", "**/*.tsx"],
    rules: {
      "react/prop-types": "off",
    },
  },
]);