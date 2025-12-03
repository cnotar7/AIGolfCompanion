

  export interface StartRound {
    courseId: number;
    teeName: string;
    courseName: string;
    userName: string;
  }

  export interface Round {
    roundId: number;
    startTime: string;
    currentHoleNumber: number;
    courseId: number;
    teeId: number;
    userName: string;
    holes: Record<number, PlayedHole>; 
    aiSummary: string;
    completed: boolean;
  }

  export interface PlayedHole {
    holeNumber: number;
    par: number;
    yardage: number;
    handicap: number;
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
  