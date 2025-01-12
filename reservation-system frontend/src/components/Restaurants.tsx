import { useEffect, useState } from "react";
import RestaurantView from "./RestaurantView";
import { toast } from "react-toastify";
import { getRestaurants } from "@/api/restaurants";
import RestaurantV from "./RestaurantV";
export interface Restaurant {
  id: number;
  name: string;
  address: string;
  description: string;
  tableCount: number;
  workingHours: string;
  cuisineType: string;
}

export default function Restaurants() {
  const [selectedRestaurant, setSelectedRestaurant] =
    useState<Restaurant | null>(null);
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const fetchRestaurants = async () => {
    try {
      const response = await getRestaurants();
      setRestaurants(response);
    } catch (error) {
      toast.error("Failed to fetch restaurants");
      console.error("Failed to fetch restaurants:", error);
    }
  };

  useEffect(() => {
    fetchRestaurants();
  }, []);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {selectedRestaurant ? (
        <RestaurantView
          restaurant={selectedRestaurant}
          onBack={() => setSelectedRestaurant(null)}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {restaurants.map((restaurant) => (
            <RestaurantV
              setSelectedRestaurant={setSelectedRestaurant}
              restaurant={restaurant}
              key={restaurant.id}
              fetchRestaurants={fetchRestaurants}
            />
          ))}
        </div>
      )}
    </div>
  );
}
