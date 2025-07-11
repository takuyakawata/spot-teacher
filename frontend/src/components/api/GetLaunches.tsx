"use client";
import { useQuery } from "@apollo/client";
import { AllLaunchesQueryDocument, Launch } from "@/gql";

export const GetLaunches = () => {
  const { data, loading, error } = useQuery(AllLaunchesQueryDocument);

  if (loading) return <div>LOADING...</div>;
  if (error) return <div>{error.message}</div>;

  return (
    <ul>
      {data?.launches?.map((launch: Launch | null, idx: number) => (
        <li key={String(idx)}>
          mission_name: {launch?.mission_name} (rocket_name: {launch?.rocket?.rocket_name})
        </li>
      ))}
    </ul>
  );
}; 