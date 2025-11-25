import { Double } from "react-native/Libraries/Types/CodegenTypes";

export interface CourseSummary {
    id: number;
    clubName: string;
    courseName: string;
    city: string;
    state: string;
    country: string;
  }
  
  export interface CourseDetail {
    id: number;
    clubName: string;
    courseName: string;
    address: string;
    city: string;
    state: string;
    country: string;
    latitude: Double;
    longitude: Double;
    tees: Tee[];
  }
  
  export interface Tee {
    teeName: string;
    courseRating: Double;
    slopeRating: Double;
    totalYards: number;
    parTotal: number
    gender: "MALE" | "FEMALE";
    holes: Hole[];

  }
  
  export interface Hole {
    par: number;
    yardage: number;
    handicap: number;
  }
  