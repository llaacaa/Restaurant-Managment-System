import axios from "axios";
const reservationServiceUrl = import.meta.env.VITE_API_RESERVATION_SERVICE;

export const getReservations = async () => {
  try {
    const response = await axios.get(reservationServiceUrl + "/reservation");
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return error;
  }
};

export const cancelReservation = async (
  token: string,
  reservationId: number,
  role: string
) => {
  const url =
    role == "MANAGER"
      ? `/reservation/${reservationId}/manager-cancel`
      : `/reservation/${reservationId}/user-cancel`;
  try {
    const response = await axios.patch(
      reservationServiceUrl + url,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response;
  } catch (error) {
    console.error("Error fetching data:", error);
    return error;
  }
};
