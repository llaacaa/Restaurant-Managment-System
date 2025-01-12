export interface Table {
  id: number;
  seats: number;
  zone: "SMOKING" | "NON_SMOKING";
  location: "INDOOR" | "OUTDOOR";
}

export interface TimeSlotData {
  id: number;
  date: string;
  time: string;
}

export interface TimeSlot {
  id: number;
  isAvailable: boolean;
  table: Table;
  timeSlot: TimeSlotData;
  restaurantId: number;
  restaurantName: string;
}

import { makeReservation, updateTimeSlots } from "@/api/timeSlots";
import { useAppSelector } from "@/store/hooks";
import { AxiosError, AxiosResponse } from "axios";
import { useState } from "react";
import { toast } from "react-toastify";

function TimeSlotItem({
  timeSlot,
  fetchTimeSlots,
  setTimeSlots,
}: {
  timeSlot: TimeSlot;
  fetchTimeSlots: () => Promise<void>;
  setTimeSlots: React.Dispatch<React.SetStateAction<TimeSlot[]>>;
}) {
  const [edit, setEdit] = useState(false);
  const [editedTimeSlot, setEditedTimeSlot] = useState<TimeSlot>(timeSlot);
  const auth = useAppSelector((state) => state.auth);

  const handleChange = (field: keyof Table | "isAvailable", value: any) => {
    if (field === "isAvailable") {
      setEditedTimeSlot((prev) => ({
        ...prev,
        isAvailable: value,
      }));
    } else {
      setEditedTimeSlot((prev) => ({
        ...prev,
        table: {
          ...prev.table,
          [field]: value,
        },
      }));
    }
  };

  const handleSubmit = async () => {
    try {
      const response = await updateTimeSlots(
        editedTimeSlot,
        auth.token as string
      );
      if (
        (response as AxiosResponse).status == 200 ||
        (response as AxiosResponse).status == 201
      ) {
        toast.success((response as AxiosResponse).data);
        setTimeSlots([]);
        fetchTimeSlots();
        setEdit(false);
      } else {
        toast.error((response as AxiosError).message);
      }
    } catch (error) {
      toast.error(error.response.data);
      console.error("Failed to update timeslot:", error);
    }
  };

  const createReservation = async () => {
    try {
      const response = await makeReservation(timeSlot.id, auth.token as string);
      if (
        (response as AxiosResponse).status == 200 ||
        (response as AxiosResponse).status == 201
      ) {
        toast.success((response as AxiosResponse).data);
        fetchTimeSlots();
      } else {
        toast.error((response as AxiosError).message);
      }
    } catch (error) {
      toast.error(error.response.data);
      console.error("Failed to create a reservation:", error);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
      <div className="p-6 text-gray-600 text-sm">
        <h3 className="text-xl font-semibold text-gray-900 mb-2">
          Table at {timeSlot.restaurantName}
        </h3>
        <div className="space-y-3">
          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Status:</span>
            {edit ? (
              <select
                value={editedTimeSlot.isAvailable.toString()}
                onChange={(e) =>
                  handleChange("isAvailable", e.target.value === "true")
                }
                className="border rounded px-2 py-1 bg-white"
              >
                <option value="true">Available</option>
                <option value="false">Not Available</option>
              </select>
            ) : (
              <span
                className={
                  editedTimeSlot.isAvailable ? "text-green-600" : "text-red-600"
                }
              >
                {editedTimeSlot.isAvailable ? "Available" : "Not Available"}
              </span>
            )}
          </div>

          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Seats:</span>
            <input
              type="number"
              disabled={!edit}
              value={editedTimeSlot.table.seats}
              onChange={(e) => handleChange("seats", parseInt(e.target.value))}
              className={`bg-white ${
                edit ? "border rounded px-2 py-1" : "border-none"
              }`}
            />
          </div>

          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Zone:</span>
            {edit ? (
              <select
                value={editedTimeSlot.table.zone}
                onChange={(e) => handleChange("zone", e.target.value)}
                className="border rounded px-2 py-1 bg-white"
              >
                <option value="SMOKING">Smoking</option>
                <option value="NON_SMOKING">Non-Smoking</option>
              </select>
            ) : (
              <span>{editedTimeSlot.table.zone}</span>
            )}
          </div>

          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Location:</span>
            {edit ? (
              <select
                value={editedTimeSlot.table.location}
                onChange={(e) => handleChange("location", e.target.value)}
                className="border rounded bg-white px-2 py-1"
              >
                <option value="INDOOR">Indoor</option>
                <option value="OUTDOOR">Outdoor</option>
              </select>
            ) : (
              <span>{editedTimeSlot.table.location}</span>
            )}
          </div>

          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Date:</span>
            <span>{new Date(timeSlot.timeSlot.date).toLocaleDateString()}</span>
          </div>

          <div className="flex items-center gap-2">
            <span className="font-medium min-w-[100px]">Time:</span>
            <span>{timeSlot.timeSlot.time}</span>
          </div>
        </div>
      </div>

      {localStorage.getItem("restaurant") === timeSlot.restaurantName &&
        auth.role == "MANAGER" && (
          <button
            onClick={() => {
              if (edit) {
                handleSubmit();
              } else {
                setEdit(true);
              }
            }}
            className={
              edit
                ? "mt-4 w-full bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 transition-colors"
                : "mt-4 w-full bg-red-500 text-white py-2 px-4 rounded-md hover:bg-red-700 transition-colors"
            }
          >
            {edit ? "Save Changes" : "Edit Timeslot"}
          </button>
        )}

      {auth.role == "CLIENT" && (
        <button
          onClick={createReservation}
          className={
            "mt-4 w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition-colors"
          }
        >
          Make reservation
        </button>
      )}
    </div>
  );
}

export default TimeSlotItem;
