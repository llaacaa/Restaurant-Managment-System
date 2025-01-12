import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getReservations } from "@/api/reservations";
import Reservation from "./Reservation";

export type ReservationStatus =
  | "CANCELLED_BY_USER"
  | "CONFIRMED"
  | "CANCELLED_BY_MANAGER";

export interface ReservationI {
  id: number;
  timeSlotTableId: number;
  userId: number;
  reservationDate: string;
  status: ReservationStatus;
  restaurantName: string;
  numberOfSeats: number;
  time: string,
  date: string
}

export default function ReservationList() {
  const [reservations, setReservations] = useState<ReservationI[]>([]);

  const fetchReservations = async () => {
    try {
      const response = await getReservations();
      setReservations([...response]);
    } catch (error) {
      toast.error("Failed to fetch reservations");
      console.error("Failed to fetch reservations:", error);
    }
  };

  useEffect(() => {
    fetchReservations();
  }, []);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {reservations.map((reservation) => (
          <Reservation
            key={reservation.id}
            reservation={reservation}
            fetchReservations={fetchReservations}
            setReservations={setReservations}
          />
        ))}
      </div>
    </div>
  );
}
