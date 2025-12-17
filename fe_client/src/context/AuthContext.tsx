import React, { createContext, useState, useContext, useEffect, ReactNode } from 'react';
import { getUserInfoApi } from '../api/userApi'; // Import the API function

// Define a user type for better type safety
interface User {
  id: number; // Add id property
  fullName: string;
  email: string;
  username: string;
  avatarUrl?: string;
  // Add other user properties as needed
}

interface AuthContextType {
  token: string | null;
  user: User | null; // Add user to the context
  loading: boolean; // Add loading state
  login: (accessToken: string, refreshToken: string) => void;
  logout: () => void;
  updateUserContext: (user: User) => void; // Add user update function
}

export const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('accessToken'));
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUserOnLoad = async () => {
      if (token) {
        try {
          const response = await getUserInfoApi();
          setUser(response.data); // Assuming response.data includes the id
        } catch (error) {
          console.error("Invalid token or failed to fetch user, logging out.", error);
          // If token is invalid, log out
          setToken(null);
          setUser(null);
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
        }
      }
      setLoading(false);
    };

    fetchUserOnLoad();
  }, [token]);

  const login = (accessToken: string, refreshToken: string) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    setToken(accessToken);
    // Fetching user info will be handled by the useEffect hook
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  };

  const updateUserContext = (newUserData: User) => {
    setUser(newUserData);
  };

  const value = { token, user, loading, login, logout, updateUserContext };

  // Render children only when not in the initial loading phase
  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
};

// Hook tùy chỉnh để dễ dàng sử dụng context trong các component khác
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};