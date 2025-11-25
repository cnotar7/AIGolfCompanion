// app/(tabs)/courses/CourseSearchScreen.tsx
import { searchForCourse } from '@/services/courses';
import { CourseSummary } from '@/types/course'; // frontend DTO
import { useRouter } from 'expo-router';
import React, { useState } from 'react';
import { ActivityIndicator, Button, FlatList, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';

export default function CourseSearchScreen() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<CourseSummary[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const router = useRouter();

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    try {
      const courses = await searchForCourse(query);
      console.log(courses)
      setResults(courses);
    } catch (err) {
      console.error('Search error:', err);
      setError('Failed to fetch courses.');
    } finally {
      setLoading(false);
    }
  };

  const handleSelectCourse = async (courseId: number) => {
    console.log("Before asdfsdfgsdfsd");
    router.push(`/courses/${courseId}`);
  };

  const renderItem = ({ item }: { item: CourseSummary }) => (
    <TouchableOpacity onPress={() => handleSelectCourse(item.id)}>
        <View style={styles.courseItem}>
            <Text style={styles.clubName}>Club Name: {item.clubName}</Text>
        <Text style={styles.courseName}>Course Name: {item.courseName}</Text>
        <Text style={styles.courseId}>ID: {item.id}</Text>
        </View>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <TextInput
        style={[styles.input, { color: '#000', backgroundColor: '#fff' }]}
        placeholder="Search for a golf course"
        value={query}
        onChangeText={setQuery}
      />
      <Button title="Search" onPress={handleSearch} />

      {loading && <ActivityIndicator style={{ marginTop: 16 }} />}
      {error.length > 0 && <Text style={styles.error}>{error}</Text>}

      <FlatList
        data={results}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderItem}
        style={{ marginTop: 16 }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 16 },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 8,
    padding: 8,
    marginBottom: 8,
  },
  courseItem: { padding: 12, borderBottomWidth: 1, borderBottomColor: '#eee' },
  courseName: { fontSize: 16, color: '#666', fontWeight: 'bold' },
  clubName: { fontSize: 16, color: '#666', fontWeight: 'bold' },
  courseId: { fontSize: 12, color: '#666' },
  error: { color: 'red', marginTop: 8 },
});
