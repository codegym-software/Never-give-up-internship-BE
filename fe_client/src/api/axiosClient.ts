import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL ,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Flag to prevent multiple token refresh requests
let isRefreshing = false;
// Queue of failed requests that are waiting for a new token
let failedQueue: any[] = [];

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

// Request interceptor
axiosClient.interceptors.request.use(
  async (config) => {
    let accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
      const decodedToken: { exp: number } = jwtDecode(accessToken);
      const isExpired = decodedToken.exp * 1000 < Date.now();

      if (isExpired) {
        if (isRefreshing) {
          // If token is already being refreshed, queue the request
          return new Promise((resolve, reject) => {
            failedQueue.push({
              resolve: (token: string) => {
                config.headers['Authorization'] = 'Bearer ' + token;
                resolve(config);
              },
              reject: (err: any) => {
                reject(err);
              }
            });
          });
        }

        isRefreshing = true;
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
          // No refresh token available, logout user
          localStorage.removeItem('accessToken');
          window.location.href = '/client/'; // Or to a login page
          return Promise.reject(new Error('No refresh token available.'));
        }

        try {
          const response = await axios.post(
            `${import.meta.env.VITE_REFESH_TOKEN_URL }`,
            { refreshToken }
          );

          const newAccessToken = response.data.accessToken;
          const newRefreshToken = response.data.refreshToken;

          localStorage.setItem('accessToken', newAccessToken);
          localStorage.setItem('refreshToken', newRefreshToken);

          accessToken = newAccessToken;
          processQueue(null, newAccessToken);
        } catch (error) {
          // Refresh failed, logout user
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          processQueue(error, null);
          window.location.href = '/client/'; // Or to a login page
          return Promise.reject(error);
        } finally {
          isRefreshing = false;
        }
      }
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Optional: Keep a response interceptor for other errors if needed,
// but the 401 logic is now handled proactively in the request interceptor.
axiosClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Handle other errors like 403, 500 etc. if necessary
    if (error.response && error.response.status === 403) {
      // e.g., redirect to an unauthorized page
    }
    return Promise.reject(error);
  }
);

export default axiosClient;