import { Restaurant } from "@/components/Restaurants";
import axios from "axios";
const reservationServiceUrl = import.meta.env.VITE_API_RESERVATION_SERVICE;

export const getRestaurants = async () => {
  try {
    const response = await axios.get(reservationServiceUrl + "/restaurant");
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error);
    throw error;
  }
};

export const updateRestaurant = async (
  token: string,
  id: number,
  restaurant: Restaurant
) => {
  try {
    const response = await axios.put(
      reservationServiceUrl + `/restaurant/${id}/update`,
      restaurant,
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
