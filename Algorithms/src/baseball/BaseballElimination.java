package baseball;

import java.util.ArrayList;
import java.util.HashMap;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	private HashMap<String, record> standings = new HashMap<>();
	private HashMap<String, Integer> index = new HashMap<>();
	private ArrayList<String> R = new ArrayList<>();
	private int N;
	private boolean is_checked;
	private class record {
		String team;
		int w, l, r;
		int[] g_ij;

		record(String team, int w, int l, int r, int[] g_ij) {
			this.w = w;
			this.l = l;
			this.r = r;
			this.g_ij = g_ij;
			this.team = team;
		}
	}

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		this.N = in.readInt();
		for (int i = 0; i < this.N; i++) {
			String team = in.readString();
			int w = in.readInt();
			int l = in.readInt();
			int r = in.readInt();
			int[] g_ij = new int[this.N];
			for (int j = 0; j < this.N; j++) {
				g_ij[j] = in.readInt();
			}
			index.put(team, i);
			standings.put(team, new record(team, w, l, r, g_ij));
		}
	}

	// number of teams
	public int numberOfTeams() {
		return standings.size();
	}

	// all teams
	public Iterable<String> teams() {
		return standings.keySet();
	}

	// number of wins for given team
	public int wins(String team) {
		checkTeam(team);
		return standings.get(team).w;
	}

	// number of losses for given team
	public int losses(String team) {
		checkTeam(team);
		return standings.get(team).l;
	}

	// number of remaining games for given team
	public int remaining(String team) {
		checkTeam(team);
		return standings.get(team).r;
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		checkTeam(team1);
		checkTeam(team2);
		return standings.get(team1).g_ij[index.get(team2)];
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		checkTeam(team);
		return checkEliminated(team);
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		checkTeam(team);
		if (!is_checked) checkEliminated(team);
		is_checked = false;
		if (R.isEmpty()) return null;
		ArrayList<String> result = new ArrayList<>(R);
		return result;
	}

	private boolean checkEliminated(String team) {
		is_checked = true;
		R = new ArrayList<>();
		// Trivial elimination
		int best = standings.get(team).w + standings.get(team).r;
		for (String t : standings.keySet()) {
			if (!t.equals(team) && standings.get(t).w > best) {
				R.add(t);
				return true;
			}
		}
		// Nontrivial elimination
		int n = N - 1 + 2 + (N - 1) * (N - 2) / 2;
		FlowNetwork fn = new FlowNetwork(n);
		HashMap<Integer, ArrayList<String>> game_vertice = new HashMap<>();
		HashMap<Integer, String> team_vertice = new HashMap<>();
		ArrayList<String> teams = new ArrayList<String>(index.keySet());
		teams.remove(team);
		constructFN(team, n, game_vertice, team_vertice, teams, fn);
//		System.out.println(fn);
		FordFulkerson FF = new FordFulkerson(fn, fn.V() - 1, 0);
		// judge s neighbors is if in cut
		for (int i = 1; i < N; i++) {
			if (FF.inCut(i)) {
				R.add(team_vertice.get(i));
			}
		}
		if (R.isEmpty()) {
			return false;
		} else {
			double wins = 0;
			double remains = 0;
			for (String R_team : R) {
				wins += standings.get(R_team).w;
			}
			for (int i = 0; i < R.size() - 1; i++) {
				for (int j = i + 1; j < R.size(); j++) {
					remains += standings.get(R.get(i)).g_ij[index.get(R.get(j))];
				}
			}
			double luck = standings.get(team).w + standings.get(team).r;
			double average = (wins + remains) / R.size();
			if (luck < average) {
				return true;
			} else {
				return false;
			}
		}
	}

	private FlowNetwork constructFN(String team, int n, HashMap<Integer, ArrayList<String>> game_vertice,
			HashMap<Integer, String> team_vertice, ArrayList<String> teams, FlowNetwork fn) {
		int vertice = 1;
		// construct team vertex
		for (String team1 : teams) {
			team_vertice.put(vertice, team1);
			vertice++;
		}
		// connect team vertices and t
		int t = 0;
		for (int id : team_vertice.keySet()) {
			double capacity = standings.get(team).w + standings.get(team).r - standings.get(team_vertice.get(id)).w;
			FlowEdge e = new FlowEdge(id, t, capacity);
			fn.addEdge(e);
		}
		int s = n - 1;
		// construct game vertex
		ArrayList<Integer> keyset = new ArrayList<Integer>();
		keyset.addAll(team_vertice.keySet());
		for (int i = 0; i < keyset.size() - 1; i++) {
			String team1 = team_vertice.get(keyset.get(i));
			for (int j = i + 1; j < keyset.size(); j++) {
				String team2 = team_vertice.get(keyset.get(j));
				ArrayList<String> tuple = new ArrayList<String>();
				tuple.add(team1);
				tuple.add(team2);
				game_vertice.put(vertice, tuple);
				FlowEdge e = new FlowEdge(s, vertice, standings.get(team1).g_ij[index.get(team2)]);
				FlowEdge e1 = new FlowEdge(vertice, keyset.get(i), Double.POSITIVE_INFINITY);
				FlowEdge e2 = new FlowEdge(vertice, keyset.get(j), Double.POSITIVE_INFINITY);
				fn.addEdge(e);
				fn.addEdge(e1);
				fn.addEdge(e2);
				vertice++;
			}
		}
		return fn;
	}
	private void checkTeam(String team) {
		if (!index.containsKey(team)) throw new java.lang.IllegalArgumentException();
	}
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}