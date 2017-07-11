package ntu.csie.stats;

public class CharacterErrorStatistics {

	public final double correct;
	public final double fixes;
	public final double incorrectFixed;
	public final double incorrectNotFixed;

	public CharacterErrorStatistics(double correct, double fixes, double incorrectFixed, double incorrectNotFixed) {
		this.correct = correct;
		this.fixes = fixes;
		this.incorrectFixed = incorrectFixed;
		this.incorrectNotFixed = incorrectNotFixed;
	}

	public double getMinimumStringDistanceErrorRate() {
		return incorrectNotFixed / (correct + incorrectFixed);
	}

	public double getCorrectionEfficiency() {
		return incorrectFixed / fixes;
	}

	public double getParticipantConscientiousness() {
		return incorrectFixed / (incorrectFixed + incorrectNotFixed);
	}

	public double getUtilisedBandwidth() {
		return correct / (correct + incorrectNotFixed + incorrectFixed + fixes);
	}

	public double getWastedBandwidth() {
		return (incorrectNotFixed + incorrectFixed + fixes) / (correct + incorrectNotFixed + incorrectFixed + fixes);
	}

	public double getTotalErrorRate() {
		return (incorrectNotFixed + incorrectFixed) / (correct + incorrectNotFixed + incorrectFixed);
	}

	public double getNotCorrectedErrorRate() {
		return incorrectNotFixed / (correct + incorrectNotFixed + incorrectFixed);
	}

	public double getCorrectedErrorRate() {
		return incorrectFixed / (correct + incorrectNotFixed + incorrectFixed);
	}

	@Override
	public String toString() {
		return String.format("[MSD: %.02f, Cerr: %.02f, NCerr: %.02f, Terr: %.02f, Ceff: %.02f, UBand: %.02f]", getMinimumStringDistanceErrorRate() * 100, getCorrectedErrorRate() * 100, getNotCorrectedErrorRate() * 100, getTotalErrorRate() * 100, getCorrectionEfficiency(), getUtilisedBandwidth() * 100);
	}

}
