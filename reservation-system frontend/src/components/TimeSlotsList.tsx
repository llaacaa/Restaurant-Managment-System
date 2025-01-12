import { useEffect, useState } from "react";
import TimeSlotItem, { TimeSlot } from "./TimeSlots";
import { toast } from "react-toastify";
import { getTimeSlots } from "@/api/timeSlots";

export default function TimeSlotsList() {
    const [timeSlots, setTimeSlots] = useState<TimeSlot[]>([]);


    const fetchTimeSlots = async () => {
      try {
        const response = await getTimeSlots();
        setTimeSlots([...response]); 
      } catch (error) {
        toast.error("Failed to fetch time slots");
        console.error("Failed to fetch time slots:", error);
      }
    };
    
  
    useEffect(() => {
      fetchTimeSlots();
    }, []);
  
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {timeSlots.map((timeSlot) => (
            <TimeSlotItem
              key={timeSlot.id}
              timeSlot={timeSlot}
              fetchTimeSlots={fetchTimeSlots}
              setTimeSlots={setTimeSlots}
            />
          ))}
        </div>
      </div>
    );
  }