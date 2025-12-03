// app/(tabs)/courses/[id].tsx
import { getCourseById } from '@/services/courses'; // create this API call
import { startRound } from '@/services/rounds';
import { CourseDetail, Tee } from '@/types/course';
import { StartRound } from '@/types/round';
import { useLocalSearchParams, useRouter } from 'expo-router';
import React, { useEffect, useState } from 'react';
import { ActivityIndicator, Button, FlatList, StyleSheet, Text, TouchableOpacity, View } from 'react-native';

export default function CourseDetailScreen() {
  const { courseId } = useLocalSearchParams();
  const [course, setCourse] = useState<CourseDetail | null>(null);
  const [selectedTee, setSelectedTee] = useState<Tee | null>(null);
  const [startingRound, setStartingRound] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const router = useRouter();

  console.log("In Course Detail Screen with id: ", courseId);
  console.log("Using Selected Tee: ", selectedTee);

  const resolvedId = Array.isArray(courseId) ? courseId[0] : courseId;

  useEffect(() => {
    if (!courseId) {
        console.log("No ID found in params");
        setError("Course ID is missing.");
        setLoading(false);
        return;
    }
    const numericId = Number(courseId);
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
  }, [courseId]);

  const handleStartRound = async () => {
    console.log("Beforesfzdfsdf");
    if (!selectedTee || ! course) return;
    console.log("Aftersdfsfds");
    setStartingRound(true);
    try {
        const startRoundPayload: StartRound = {
            courseId: course.id,
            courseName: course.courseName,
            teeName: selectedTee.teeName,
            userName: 'demoUserName', //replace with real userName when adding auth
        };
        
        const round = await startRound(startRoundPayload);
        console.log("Round Returned = ", round);
        

        router.push({
          pathname: '/rounds/[roundId]',
          params: {
            roundId: round.roundId.toString(),
            round: JSON.stringify(round),
          },
        });

    } catch (err) {
        console.error(err);
        setError('Failed to start round');
        router.back();
    } finally {
        setStartingRound(false);
    }
  }


  if (loading) return <ActivityIndicator style={{ marginTop: 16 }} />;

  if (error) return <Text style={styles.error}>{error}</Text>;

  if (!course) return null;

  // Finally display the course
  return (
    <View style={styles.container}>
      <Text style={styles.title}>{course.courseName}</Text>
      <Text style={styles.subtitle}>{course.clubName}</Text>
      
      <Text style={styles.subtitle}> {course.address}, {course.city}, {course.state}, {course.country}</Text>

      <Text style={styles.teeTitle}>Select a Tee:</Text>
      <FlatList
        data={course.tees}
        keyExtractor={(item) => item.teeName}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={[
              styles.teeItem,
              selectedTee?.teeName === item.teeName && styles.teeItemSelected
            ]}
            onPress={() => setSelectedTee(item)}
          >
            <Text style={styles.teeTitle}>{item.teeName} - {item.totalYards}yd, Par {item.parTotal}</Text>
            <Text style={styles.teeText}>Course Rating - {item.courseRating}, Slope Rating - {item.slopeRating}</Text>
          </TouchableOpacity>
        )}
      />

      <View style={styles.buttonContainer}>
        <Button title="Go Back" onPress={() => router.back()} />
        <Button title="Start Round" onPress={handleStartRound} disabled={!selectedTee || startingRound} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
    container: { flex: 1, padding: 16, backgroundColor: '#000' },
    title: { fontSize: 22, fontWeight: 'bold', color: '#fff', marginBottom: 4 },
    subtitle: { fontSize: 16, color: '#ccc', marginBottom: 8 },
    teeTitle: { fontSize: 18, fontWeight: 'bold', color: '#fff', marginTop: 16, marginBottom: 8 },
    teeItem: { padding: 12, borderWidth: 1, borderColor: '#555', borderRadius: 8, marginBottom: 8 },
    teeItemSelected: { backgroundColor: '#1E90FF', borderColor: '#fff' },
    teeText: { color: '#fff' },
    buttonContainer: { flexDirection: 'row', justifyContent: 'space-between', marginTop: 16 },
    error: { color: 'red', marginTop: 8 },
  });