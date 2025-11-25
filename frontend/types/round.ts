export interface ShotRequest {
    club: string;
    result: string;
  }
  
  export interface Shot {
    id: number;
    shotNumber: number;
    club: string;
    result: string;
  }
  
  export interface PlayedHole {
    holeNumber: number;
    par: number;
    strokes: number;
    putts: number;
    completed: boolean;
    shots: Shot[];
  }
  
  export interface Round {
    id: number;
    currentHoleNumber: number;
    courseName: string;
    holes: Record<number, PlayedHole>;
    completed: boolean;
  }

  export interface StartRound {
    courseId: number;
    teeId: number;
    courseName: string;
    userName: string;
  }

  export interface Shot {
    club: string;
    shotNumber: number;
    result: string;
  }

  export interface RoundResponse {
    roundId: number;
    courseId: number;
    teeId: number;
    courseName: string;
    userName: string;
    currentHoleNumber: number;
    startTime: string;
    completed: boolean;
  }

  export interface PlayedHole {
    holeNumber: number;
    par: number;
    strokes: number;
    putts: number;
    completed: boolean;
    shots: Shot[];
  }

  export interface Shot {
    club: string;
    shotNumber: number;
    result: string;
  }
  