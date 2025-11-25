// app/(tabs)/courses/[id].tsx
import { getCourseById } from '@/services/courses'; // create this API call
import { CourseDetail } from '@/types/course';
import { useLocalSearchParams } from 'expo-router';
import React, { useEffect, useState } from 'react';
import { ActivityIndicator, ScrollView, StyleSheet, Text, View } from 'react-native';

export default function CourseDetailScreen() {
  const { id } = useLocalSearchParams();
  const [course, setCourse] = useState<CourseDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  console.log("In Course Detail Screen with id: ", id);

  const resolvedId = Array.isArray(id) ? id[0] : id;

  useEffect(() => {
    if (!resolvedId) {
        console.log("No ID found in params");
        setError("Course ID is missing.");
        setLoading(false);
        return;
    }
    const numericId = Number(resolvedId);
    console.log("➡️ Fetching course details for ID:", numericId);

    const fetchCourse = async () => {
        setLoading(true);
        try {
            console.log("Before Call");
            const data = await getCourseById(numericId);
            console.log("Recieved Course Data: ", data);
            setCourse(data);
        } catch (err) {
            console.error(err);
            setError('Failed to fetch course details.');
        } finally {
            setLoading(false);
        }
    };

    fetchCourse();
  }, [resolvedId]);

  if (loading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator color="#fff" />
        <Text style={styles.loadingText}>Loading course details…</Text>
      </View>
    );
  }

  if (error || !course) {
    return (
      <View style={styles.center}>
        <Text style={styles.error}>{error || 'Course not found.'}</Text>
      </View>
    );
  }

  // Finally display the course
  return (
    <ScrollView style={styles.container}>
      <Text style={styles.header}>{course.courseName}</Text>
      <Text style={styles.subHeader}>{course.clubName}</Text>

      <View style={styles.section}>
        <Text style={styles.label}>Address:</Text>
        <Text style={styles.value}>
          {course.address}, {course.city}, {course.state}, {course.country}
        </Text>
      </View>

      <View style={styles.section}>
        <Text style={styles.label}>Tees:</Text>

        {course.tees.length === 0 && (
          <Text style={styles.value}>No tees available.</Text>
        )}

        {course.tees.map((tee, index) => (
          <View key={index} style={styles.teeBox}>
            <Text style={styles.teeName}>{tee.teeName}</Text>
            <Text style={styles.teeDetail}>Course Rating: {tee.courseRating}</Text>
            <Text style={styles.teeDetail}>Slope Rating: {tee.slopeRating}</Text>
            <Text style={styles.teeDetail}>Par: {tee.parTotal}</Text>
            <Text style={styles.teeDetail}>Yardage: {tee.totalYards}</Text>
          </View>
        ))}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
    padding: 16,
  },

  center: {
    flex: 1,
    backgroundColor: '#000',
    justifyContent: 'center',
    alignItems: 'center',
  },

  loadingText: { color: '#fff', marginTop: 8 },
  error: { color: 'red', fontSize: 16 },

  header: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 4,
  },
  subHeader: {
    color: '#ccc',
    fontSize: 18,
    marginBottom: 16,
  },

  section: {
    marginTop: 16,
  },
  label: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 16,
  },
  value: {
    color: '#ccc',
    fontSize: 14,
    marginTop: 4,
  },

  teeBox: {
    marginTop: 12,
    padding: 10,
    borderWidth: 1,
    borderColor: '#333',
    borderRadius: 8,
    backgroundColor: '#111',
  },
  teeName: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 16,
  },
  teeDetail: {
    color: '#ccc',
    fontSize: 14,
    marginTop: 2,
  },
});