import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface AuthState {
  token: string | null;
  username: string | null;
  isAuthenticated: boolean;
  role: string | null;
  id: string | null;
}

const initialState: AuthState = {
  token: localStorage.getItem("token"),
  username: localStorage.getItem("username"),
  isAuthenticated: !!localStorage.getItem("token"),
  role: localStorage.getItem("role"),
  id: localStorage.getItem("id"),
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{
        token: string;
        username: string;
        role: string;
        id: string;
      }>
    ) => {
      const { token, username, role, id } = action.payload;
      state.token = token;
      state.username = username;
      state.isAuthenticated = true;
      state.role = role;
      state.id = id;

      localStorage.setItem("token", token);
      localStorage.setItem("username", username);
      localStorage.setItem("role", role);
      localStorage.setItem("id", id)
    },
    logout: (state) => {
      state.token = null;
      state.username = null;
      state.isAuthenticated = false;
      state.role = null;
      state.id = null;

      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("role");
      localStorage.removeItem("id");
      localStorage.removeItem("restaurant");
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
const authReducer = authSlice.reducer;
export default authReducer;
