import { makeTimeSlot } from "@/api/timeSlots";
import { useAppSelector } from "@/store/hooks";
import { AxiosError, AxiosResponse } from "axios";
import { useState } from "react";
import { toast } from "react-toastify";

export type FormDataTimeSlot = {
  date: string;
  time: string;
};

const CreateTimeSlots = ({ restaurantId }: { restaurantId: number }) => {
  const [formData, setFormData] = useState({
    date: "",
    time: "",
    restaurantId,
  });

  const auth = useAppSelector((state) => state.auth);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await makeTimeSlot(
        formData as FormDataTimeSlot,
        auth.token as string
      );
      if (
        (response as AxiosResponse).status == 200 ||
        (response as AxiosResponse).status == 201
      ) {
        toast((response as AxiosResponse).data);
      } else {
        toast((response as AxiosError).message);
      }
    } catch (error) {
      toast.error(error.response.data);
      console.error("Failed to create a timeslot:", error);
    }
  };

  return (
    <div className="max-w-md mx-auto p-6 m-10 bg-white shadow-lg rounded-3xl">
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label
            htmlFor="date"
            className="block text-gray-700 font-semibold mb-2"
          >
            Date
          </label>
          <input
            type="date"
            id="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div className="mb-4">
          <label
            htmlFor="time"
            className="block text-gray-700 font-semibold mb-2"
          >
            Time
          </label>
          <input
            type="time"
            id="time"
            name="time"
            value={formData.time}
            onChange={handleChange}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button
          type="submit"
          className="w-full bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          Create new time slot
        </button>
      </form>
    </div>
  );
};

export default CreateTimeSlots;
