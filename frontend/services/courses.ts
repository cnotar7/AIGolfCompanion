import { CourseDetail, CourseSummary } from "@/types/course";
import { api } from "./api";
  
  export async function searchForCourse(query: string) {
    const res = await api.get<CourseSummary[]>(`/courses?query=${query}`);
    return res.data;
  }

  export async function getCourseById(courseId: number) {
    const res = await api.get<CourseDetail>(`/courses/${courseId}`);
    return res.data;
  }