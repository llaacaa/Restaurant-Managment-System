import { useAppSelector } from "@/store/hooks";
import CreateTimeSlots from "./CreateTimeSlots";
import { Restaurant } from "./Restaurants";

interface RestaurantViewProps {
  restaurant: Restaurant;
  onBack: () => void;
}

export default function RestaurantView({
  restaurant,
  onBack,
}: RestaurantViewProps) {
  const auth = useAppSelector((state) => state.auth);
  return (
    <div className="bg-white rounded-lg shadow-md p-8">
      {auth.role == "MANAGER" &&
        restaurant.name == localStorage.getItem("restaurant") && (
          <div className="flex w-full justify-center">
            <CreateTimeSlots restaurantId={restaurant.id} />
          </div>
        )}
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-bold text-gray-900">{restaurant.name}</h2>
        <button
          onClick={onBack}
          className="text-slate-600 hover:text-slate-300"
        >
          ← Back to list
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="space-y-4">
          <div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              Description
            </h3>
            <p className="text-gray-600">{restaurant.description}</p>
          </div>

          <div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              Location
            </h3>
            <p className="text-gray-600">{restaurant.address}</p>
          </div>
        </div>

        <div className="space-y-4">
          <div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              Details
            </h3>
            <div className="space-y-2">
              <p className="text-gray-600">
                <span className="font-medium">Cuisine Type:</span>{" "}
                {restaurant.cuisineType}
              </p>
              <p className="text-gray-600">
                <span className="font-medium">Working Hours:</span>{" "}
                {restaurant.workingHours}
              </p>
              <p className="text-gray-600">
                <span className="font-medium">Number of Tables:</span>{" "}
                {restaurant.tableCount}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
