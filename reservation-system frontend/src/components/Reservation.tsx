import React from "react";
import { ReservationI } from "./ReservationList";
import { useAppSelector } from "@/store/hooks";
import { cancelReservation } from "@/api/reservations";
import { toast } from "react-toastify";
import { AxiosError, AxiosResponse } from "axios";

function Reservation({
  reservation,
  fetchReservations,
  setReservations,
}: {
  reservation: ReservationI;
  fetchReservations: () => Promise<void>;
  setReservations: React.Dispatch<React.SetStateAction<ReservationI[]>>;
}) {
    const auth = useAppSelector(state => state.auth);
    const condition = (auth.role == "MANAGER" && reservation.restaurantName == localStorage.getItem("restaurant")) || (auth.role == "CLIENT" && reservation.userId == (auth.id as unknown))

  const handleCancel = async () => {
      try {
        const response = await cancelReservation((auth.token as string), reservation.id, (auth.role as string));
          if (
            (response as AxiosResponse).status == 200 ||
            (response as AxiosResponse).status == 201
          ) {
            toast.success((response as AxiosResponse).data);
            setReservations([]);
            fetchReservations();
          } else {
            toast.error((response as AxiosError).message);
          }
        } catch (error) {
          toast.error(error.response.data);
          console.error("Failed to create a reservation:", error);
        }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow max-w-md mx-auto">
      <div className="p-6">
        <h3 className="text-xl font-semibold text-gray-900 mb-4">
          Reservation at {reservation.restaurantName}
        </h3>
        <div className="space-y-3 text-gray-700">
          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Status:</span>
            <span
              className={`${
                reservation.status === "CONFIRMED"
                  ? "text-green-600"
                  : "text-red-600"
              }`}
            >
              {reservation.status}
            </span>
          </div>
          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Seats:</span>
            <span>{reservation.numberOfSeats}</span>
          </div>
          {/* <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Table ID:</span>
            <span>{reservation.timeSlotTableId}</span>
          </div> */}
          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Date:</span>
            <span>
              {new Date(reservation.date).toLocaleDateString()}
            </span>
          </div>
          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Time:</span>
            <span>{reservation.time}</span>
          </div>
        </div>
      </div>
      {condition && reservation.status == "CONFIRMED" && <button
        onClick={handleCancel}
        className="w-full bg-red-500 text-white py-2 px-4 rounded-md hover:bg-red-600 transition-colors"
      >
        Cancel Reservation
      </button>}
    </div>
  );
}

export default Reservation;
