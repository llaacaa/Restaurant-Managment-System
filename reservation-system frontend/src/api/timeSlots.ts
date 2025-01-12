import { FormDataTimeSlot } from "@/components/CreateTimeSlots";
import { TimeSlot } from "@/components/TimeSlots";
import axios from "axios";
const reservationServiceUrl = import.meta.env.VITE_API_RESERVATION_SERVICE;

export const getTimeSlots = async () => {
  try {
    const response = await axios.get(
      reservationServiceUrl + "/free-time-slots"
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching data:", error);
    return error;
  }
};

export const updateTimeSlots = async (timeslot: TimeSlot, token: string) => {
  const payload = {
    tableId: timeslot.table.id,
    seats: timeslot.table.seats,
    zone: timeslot.table.zone,
    location: timeslot.table.location,
    timeSlotId: timeslot.id,
    availability: timeslot.isAvailable,
  };

  try {
    const response = await axios.post(
      reservationServiceUrl + `/table/${timeslot.restaurantId}/update`,
      payload,
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

// restaurantId param

//tableid
//seats
//zone
//location
//timeslotid
//availability

export const makeReservation = async (
  timeslottableId: number,
  token: string
) => {
  try {
    const response = await axios.post(
      reservationServiceUrl + `/reservation/${timeslottableId}`,
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

export const makeTimeSlot = async (
  formData: FormDataTimeSlot,
  token: string
) => {
  try {
    const response = await axios.post(
      reservationServiceUrl + `/time-slot/create`,
      formData,
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
