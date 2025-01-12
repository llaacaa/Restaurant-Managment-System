import {
  ClientFormData,
  ManagerFormData,
} from "@/components/auth/RegisterForm";
import axios from "axios";
const userServiceUrl = import.meta.env.VITE_API_USER_SERVICE;

export const loginApi = async (loginData: {
  username: string;
  password: string;
}) => {
  try {
    const response = await axios.post(userServiceUrl + "/user/login", {
      username: loginData.username,
      password: loginData.password,
    });
    return response;
  } catch (error: unknown) {
    console.log(error);
    return error;
  }
};

export const registerManager = async (managerData: ManagerFormData) => {
  try {
    const response = await axios.post(
      userServiceUrl + "/manager/register",
      managerData
    );
    return response;
  } catch (error: unknown) {
    console.log(error);
    return error;
  }
};

export const registerClient = async (clientData: ClientFormData) => {
  try {
    const response = await axios.post(
      userServiceUrl + "/client/register",
      clientData
    );
    return response;
  } catch (error: unknown) {
    console.log(error);
    return error;
  }
};
