// app/(tabs)/rounds/[id].tsx
import { addShot, deleteShot, moveToNextHole, moveToPreviousHole, updateShot } from '@/services/rounds';
import { Round, Shot } from '@/types/round';
import { useLocalSearchParams, useRouter } from 'expo-router';
import React, { useEffect, useState } from 'react';
import { ActivityIndicator, Button, StyleSheet, Text, TextInput, View } from 'react-native';

export default function RoundScreen() {
  const router = useRouter();
  const { roundId } = useLocalSearchParams();
  const { round: roundParam } = useLocalSearchParams<{ round: string }>();
  const [round, setRound] = useState<Round>(roundParam ? JSON.parse(roundParam) : null);
  const [currentShotNumber, setCurrentShotNumber] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [editingShotId, setEditingShotId] = useState<number | null>(null);
  const [addingShot, setAddingShot] = useState(false);



  // Compute shot number *only when round or hole changes*
  useEffect(() => {
    if (!round) return;
    const nextShotNum = round.holes[round.currentHoleNumber].shots.length + 1;
    setCurrentShotNumber(nextShotNum);
  }, [round]);

  const [newShot, setNewShot] = useState<Partial<Shot>>({
    club: '',
    result: '',
    shotNumber: 1,
  });

  // Keep shotNumber in sync with computed currentShotNumber
  useEffect(() => {
    setNewShot((s) => ({ ...s, shotNumber: currentShotNumber }));
  }, [currentShotNumber]);

  const handleAddShot = async () => {
    if (!round) return;
    setLoading(true);
    setError('');

    try {
      console.log("Sending this Shot: ", newShot);
      const updatedRound = await addShot(round.roundId, round.currentHoleNumber, newShot as Shot);
      setRound(updatedRound);
      setAddingShot(false);
      setNewShot({ club: '', result: '' });
    } catch (err) {
      console.error(err);
      setError('Failed to add shot.');
    } finally {
      setLoading(false);
    }
  };


  const handleNextHole = async () => {
    if (!round) return;
    setLoading(true);
    setError('');

    try {
      const updatedRound = await moveToNextHole(round.roundId);
      setRound(updatedRound);
      setEditingShotId(null);
      setAddingShot(false);
      setNewShot({ club: '', result: '' });
    } catch (err) {
      console.error(err);
      setError('Failed to move to next hole.');
    } finally {
      setLoading(false);
    }
  };


  const handlePreviousHole = async () => {
    if (!round) return;
    setLoading(true);
    setError('');

    try {
      const updatedRound = await moveToPreviousHole(round.roundId);
      setRound(updatedRound);
      setEditingShotId(null);
      setAddingShot(false);
      setNewShot({ club: '', result: '' });
    } catch (err) {
      console.error(err);
      setError('Failed to move to previous hole.');
    } finally {
      setLoading(false);
    }
  };


  const handleBeginEditShot = (shot: Shot) => {
    setEditingShotId(shot.shotId);
    setNewShot({
      club: shot.club,
      result: shot.result,
      shotNumber: shot.shotNumber,
    });
  };

  const handleSaveEditShot = async () => {
    if (!round || !editingShotId) return;
  
    setLoading(true);
    setError('');
  
    try {
      const updatedHole = await updateShot(editingShotId, newShot as Shot);
  
      setRound((prev) => ({
        ...prev,
        holes: {
          ...prev.holes,
          [round.currentHoleNumber]: updatedHole,
        }
      }));
  
      setEditingShotId(null);
      setNewShot({ club: '', result: '' });
  
    } catch (err) {
      console.error(err);
      setError('Failed to update shot.');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setEditingShotId(null);
    setNewShot({ club: '', result: '' });
  };

  const handleDeleteShot = async (shotId: number) => {
    if (!round) return;
  
    setLoading(true);
    setError('');
  
    try {
      const updatedHole = await deleteShot(shotId);
  
      setRound((prev) => ({
        ...prev,
        holes: {
          ...prev.holes,
          [round.currentHoleNumber]: updatedHole,
        }
      }));
  
      // Reset form if deleting while editing
      if (editingShotId === shotId) {
        setEditingShotId(null);
        setNewShot({ club: '', result: '' });
      }
  
    } catch (err) {
      console.error(err);
      setError('Failed to delete shot.');
    } finally {
      setLoading(false);
    }
  };
  
  
  
  

  if (!round) {
    return (
      <View style={styles.container}>
        {loading ? <ActivityIndicator /> : <Text style={styles.error}>{error}</Text>}
      </View>
    );
  }

  const currentHole = round.holes[round.currentHoleNumber];
  console.log("Current Hole: ", currentHole);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Round at </Text>
      <Text style={styles.hole}>Hole {currentHole.holeNumber}</Text>
      <Text style={styles.par}>Par: {currentHole.par}</Text>
      <Text style={styles.yards}>Yards: {currentHole.yardage}</Text>
      <Text style={styles.yards}>Handicap: {currentHole.handicap}</Text>

      <View style={styles.buttonContainer}>
        <Button
          title="Previous Hole"
          onPress={handlePreviousHole}
          disabled={round.currentHoleNumber === 1 || loading}
        />
        <Button
          title="Next Hole"
          onPress={handleNextHole}
          disabled={round.currentHoleNumber === 18 || loading}
        />
      </View>

      <View style={{ marginTop: 16 }}>
        <Text style={styles.shotsHeader}>{currentShotNumber > 1 ? 'Shots:' : ''}</Text>

        {currentHole.shots.map((shot, index) => (
          <View key={shot.shotId} style={styles.shotRow}>
            <Text style={styles.shotText}>
              {index + 1}. {shot.club} {shot.result ? `(${shot.result})` : ''}
            </Text>

            <Button title="Edit" onPress={() => handleBeginEditShot(shot)} />
            <View style={{ width: 6 }} />
            <Button title="Delete" color="red" onPress={() => handleDeleteShot(shot.shotId)} />
          </View>
        ))}
      </View>

      {/* ADD SHOT BUTTON OR FORM */}
      <View style={{ marginTop: 20 }}>

      {/* When NOT editing and NOT adding → show Add Shot button */}
      {!editingShotId && !addingShot && (
        <Button title="Add Shot" onPress={() => setAddingShot(true)} />
      )}

      {/* When adding OR editing → show form */}
      {(addingShot || editingShotId) && (
        <View style={styles.formContainer}>
          <Text style={{ color: '#fff', fontWeight: 'bold', marginBottom: 8 }}>
            {editingShotId ? "Edit Shot" : "Add Shot"}
          </Text>

          <TextInput
            placeholder="Club"
            value={newShot.club}
            onChangeText={(text) => setNewShot((s) => ({ ...s, club: text }))}
            style={styles.input}
            placeholderTextColor="#777"
          />

          <TextInput
            placeholder="Result"
            value={newShot.result || ''}
            onChangeText={(text) => setNewShot((s) => ({ ...s, result: text }))}
            style={styles.input}
            placeholderTextColor="#777"
          />

          <Button
            title={editingShotId ? "Save Changes" : "Add Shot"}
            onPress={editingShotId ? handleSaveEditShot : handleAddShot}
          />

          {/* Cancel button */}
          <View style={{ marginTop: 8 }}>
            <Button
              title="Cancel"
              color="gray"
              onPress={() => {
                setAddingShot(false);
                setEditingShotId(null);
                setNewShot({ club: '', result: '' });
              }}
            />
          </View>
        </View>
      )}
      </View>

    </View>

  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "#0b0b0b",
  },

  title: {
    fontSize: 22,
    fontWeight: "700",
    color: "#ffffff",
    marginBottom: 8,
  },

  hole: {
    fontSize: 20,
    fontWeight: "600",
    color: "#f5f5f5",
    marginTop: 4,
  },

  par: {
    fontSize: 16,
    color: "#bdbdbd",
    marginTop: 2,
  },

  yards: {
    fontSize: 16,
    color: "#bdbdbd",
    marginTop: 2,
    marginBottom: 12,
  },

  shotsHeader: {
    color: "#fff",
    fontWeight: "bold",
    fontSize: 16,
    marginBottom: 6,
  },

  shotRow: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#1b1b1b",
    padding: 12,
    borderRadius: 8,
    marginBottom: 8,
    borderColor: "#333",
    borderWidth: 1,
  },

  shotText: {
    color: "#e0e0e0",
    flex: 1,
    fontSize: 15,
  },

  formContainer: {
    marginTop: 16,
    backgroundColor: "#1a1a1a",
    padding: 16,
    borderRadius: 10,
    borderWidth: 1,
    borderColor: "#333",
  },

  formTitle: {
    color: "#ffffff",
    fontWeight: "700",
    fontSize: 18,
    marginBottom: 12,
  },

  input: {
    backgroundColor: "#2a2a2a",
    color: "#fff",
    marginBottom: 12,
    padding: 12,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: "#444",
    fontSize: 16,
  },

  buttonContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 20,
  },

  error: {
    color: "red",
    marginTop: 6,
  }
});
