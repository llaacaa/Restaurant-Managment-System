import {
  ClientFormData,
  ManagerFormData,
} from "@/components/auth/RegisterForm";
import axios from "axios";

const userServiceUrl = import.meta.env.VITE_API_USER_SERVICE;

export const updateUser = async (token:string, userData: ManagerFormData | ClientFormData, id:number) => {
  try {
    const response = await axios.patch(
      userServiceUrl + "/user/update",
      {
          id,
          ...userData
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response;
  } catch (error: unknown) {
    console.log(error);
    return error;
  }
};
