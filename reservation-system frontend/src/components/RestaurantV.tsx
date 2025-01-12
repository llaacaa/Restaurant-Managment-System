import { useState } from "react";
import { Restaurant } from "./Restaurants";
import { updateRestaurant } from "@/api/restaurants";
import { toast } from "react-toastify";
import { useAppSelector } from "@/store/hooks";
import { AxiosError, AxiosResponse } from "axios";

function RestaurantV({
  restaurant,
  setSelectedRestaurant,
  fetchRestaurants,
}: {
  restaurant: Restaurant;
  setSelectedRestaurant: React.Dispatch<
    React.SetStateAction<Restaurant | null>
  >;
  fetchRestaurants: () => Promise<void>;
}) {
  const [edit, setEdit] = useState(false);
  const [editedRestaurant, setEditedRestaurant] =
    useState<Restaurant>(restaurant);

  const auth = useAppSelector((state) => state.auth);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    field: keyof Restaurant
  ) => {
    setEditedRestaurant((prev) => ({
      ...prev,
      [field]: e.target.value,
    }));
  };

  const handleSubmit = async () => {
    try {
      const response: AxiosResponse | unknown = await updateRestaurant(
        auth.token as string,
        restaurant.id,
        editedRestaurant
      );

      if (
        (response as AxiosResponse).status == 200 ||
        (response as AxiosResponse).status == 201
      ) {
        setEdit(false);
        fetchRestaurants();
        toast.success("Successfully updated restaurant!");
      } else {
        toast.error((response as AxiosError).message);
      }
    } catch (error) {
      console.error(error);
      toast.error("Failed to update restaurant");
    }
  };

  return (
    <div
      key={restaurant.id}
      className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
    >
      <div className="p-6 text-gray-600 text-sm">
        <h3 className="text-xl font-semibold text-gray-900 mb-2">
          <input
            disabled={!edit}
            value={editedRestaurant.name}
            onChange={(e) => handleChange(e, "name")}
            className={`bg-white w-full ${
              edit ? "border rounded px-2 py-1" : "border-none"
            }`}
          />
        </h3>
        <p className="mb-4">
          <input
            disabled={!edit}
            value={editedRestaurant.description}
            onChange={(e) => handleChange(e, "description")}
            className={`bg-white w-full ${
              edit ? "border rounded px-2 py-1" : "border-none"
            }`}
          />
        </p>
        <div className="space-y-2">
          <p className="flex items-center gap-2">
            <span className="font-medium min-w-[80px]">Address:</span>
            <input
              disabled={!edit}
              value={editedRestaurant.address}
              onChange={(e) => handleChange(e, "address")}
              className={`bg-white flex-1 ${
                edit ? "border rounded px-2 py-1" : "border-none"
              }`}
            />
          </p>
          <p className="flex items-center gap-2">
            <span className="font-medium min-w-[80px]">Tables:</span>
            <input
              disabled={!edit}
              type="number"
              value={editedRestaurant.tableCount}
              onChange={(e) => handleChange(e, "tableCount")}
              className={`bg-white ${
                edit ? "border rounded px-2 py-1" : "border-none"
              }`}
            />
          </p>
          <p className="flex items-center gap-2">
            <span className="font-medium min-w-[80px]">Hours:</span>
            <input
              disabled={!edit}
              value={editedRestaurant.workingHours}
              onChange={(e) => handleChange(e, "workingHours")}
              className={`bg-white flex-1 ${
                edit ? "border rounded px-2 py-1" : "border-none"
              }`}
            />
          </p>
          <p className="flex items-center gap-2">
            <span className="font-medium min-w-[80px]">Cuisine:</span>
            <input
              disabled={!edit}
              value={editedRestaurant.cuisineType}
              onChange={(e) => handleChange(e, "cuisineType")}
              className={`bg-white ${
                edit ? "border rounded px-2 py-1" : "border-none"
              }`}
            />
          </p>
        </div>
        <button
          onClick={() => setSelectedRestaurant(restaurant)}
          className="mt-4 w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition-colors"
        >
          View Restaurant
        </button>
        {localStorage.getItem("restaurant") === restaurant.name && (
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
            {edit ? "Confirm changes" : "Edit Restaurant"}
          </button>
        )}
      </div>
    </div>
  );
}

export default RestaurantV;
