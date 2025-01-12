const userServiceUrl = import.meta.env.VITE_API_USER_SERVICE;

import axios, { AxiosResponse } from "axios";

const fetchData = async (token: string): Promise<AxiosResponse | void> => {
  try {
    const response = await axios.get(userServiceUrl + "/user", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
};

export default fetchData;

export const getUserById = async (id: string) => {
  try {
    const response = await axios.get(userServiceUrl + "/manager/" + id);
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
};

export const getUsers = async () => {
  try {
    const response = await axios.get(userServiceUrl + "/user/all");
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
};

export const blockUser = async (token: string, id: number) => {
  try {
    const response = await axios.post(
      `${userServiceUrl}/admin/${id}/toggle-block`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error disabling user:", error);
    throw error;
  }
};
