export interface PlayerStats {
    totalRounds: number;
    averageScore: number;
    averagePutts: number;
    handicapEstimate: number | null;
    averageScoreByHole: Record<number, number>;
  }
  