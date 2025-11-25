import { PlayedHole, RoundResponse, Shot, StartRound } from "@/types/round";
import { api } from "./api";

export async function startRound(startRound: StartRound) {
  const res = await api.post<RoundResponse>(`/rounds`, { startRound });
  return res.data;
}

export async function getRound(roundId: number) {
  const res = await api.get<RoundResponse>(`/rounds/${roundId}`);
  return res.data;
}

export async function getCurrentHole(roundId: number) {
  const res = await api.get<PlayedHole>(`/rounds/${roundId}/holes/current`);
  return res.data;
}

export async function getSpecificHole(roundId: number, holeNumber: number) {
  const res = await api.get<PlayedHole>(`/rounds/${roundId}/holes/${holeNumber}`);
  return res.data;
}

export async function updateShot(shotId: number, shot: Shot) {
  const res = await api.put(`/rounds/shots/${shotId}`, shot);
  return res.data;
}

export async function deleteShot(shotId: number) {
  const res = await api.delete(`/rounds/shots/${shotId}`);
  return res.data;
}

export async function moveToNextHole(roundId: number) {
  const res = await api.post(`/rounds/${roundId}/holes/next`);
  return res.data;
}

export async function moveToPreviousHole(roundId: number) {
  const res = await api.post(`/rounds/${roundId}/holes/previous`);
  return res.data;
}

export async function completeRound(roundId: number) {
  return api.post(`/rounds/${roundId}/complete`);
}
