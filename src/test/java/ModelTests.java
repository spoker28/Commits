import org.commits.Model;
import org.commits.entities.commits.ChangerCommit;
import org.commits.entities.commits.Commit;
import org.commits.entities.commits.CreationCommit;
import org.commits.entities.commits.DeletionCommit;
import org.commits.entities.Module;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelTests {
    @Test
    public void TestChangeSnapshotSingle() {
        Model model = new Model();
        CreationCommit commit = new CreationCommit(0, "first", "Dasd");
        model.changeSnapshot(commit);
        Module root = model.getRoot();
        assertEquals(root.getChilds().size(), 1);
        assertEquals(root.getName(), "");
        assertEquals(root.getData(), "");
        assertEquals(root.getChilds().get(commit.getName()).getName(), commit.getName());
        assertEquals(root.getChilds().get(commit.getName()).getName(), "first");
        assertEquals(root.getChilds().get(commit.getName()).getData(), commit.getData());
        assertEquals(root.getChilds().get(commit.getName()).getData(), "Dasd");

        ChangerCommit changerCommit = new ChangerCommit(1, "first", "Changed data");
        model.changeSnapshot(changerCommit);
        assertEquals(root.getChilds().size(), 1);
        assertEquals(root.getName(), "");
        assertEquals(root.getData(), "");
        assertEquals(root.getChilds().get(changerCommit.getName()).getName(), changerCommit.getName());
        assertEquals(root.getChilds().get(changerCommit.getName()).getName(), "first");
        assertEquals(root.getChilds().get(changerCommit.getName()).getData(), changerCommit.getData());
        assertEquals(root.getChilds().get(changerCommit.getName()).getData(), "Changed data");

        DeletionCommit deleterCommit = new DeletionCommit(2, "first");
        model.changeSnapshot(deleterCommit);
        assertEquals(root.getChilds().size(), 0);
        assertEquals(root.getName(), "");
        assertEquals(root.getData(), "");
    }

    @Test
    public void TestChangeSnapshotArrayList() {
        Model model = new Model();
        Module root = model.getRoot();
        ArrayList<Commit> commits = new ArrayList<Commit>();
        int k = 0;
        for (int i = 0; i < 3; ++i) {
            CreationCommit first = new CreationCommit(k++, "_" + Integer.toString(i), "");
            commits.add(first);
            for (int j = 0; j < 10; ++j) {
                CreationCommit second = new CreationCommit(k++,
                        "_" + Integer.toString(i) + "_" + Integer.toString(j)
                        , Integer.toString(i) + "a" + Integer.toString(j));
                commits.add(second);
                if (i % 4 == 0) {
                    for (int z = 0; z < 2; ++z) {
                        CreationCommit commit = new CreationCommit(k++,
                                "_" + Integer.toString(i) + "_" + Integer.toString(j) + "_" + Integer.toString(z)
                                , Integer.toString(i) + "a" + Integer.toString(j) + "a" + Integer.toString(z));
                        commits.add(commit);
                    }
                }
            }
        }
        model.changeSnapshot(commits);
        assertEquals(root.getChilds().size(), 3);
        assertEquals(root.getName(), "");
        assertEquals(root.getData(), "");//TODO переписать циклы для HashMap и изменить DeletoinCommit и ChangerCommit
        for (int i = 0; i < 3; ++i) {
            assertEquals(root.getChilds().get(Integer.toString(i)).getName(), Integer.toString(i));
            assertEquals(root.getChilds().get(Integer.toString(i)).getData(), "");
            Module cur = root.getChilds().get(Integer.toString(i));
            for (int j = 0; j < 10; ++j) {
                assertEquals(cur.getChilds().get(Integer.toString(j)).getName(), Integer.toString(j));
                assertEquals(cur.getChilds().get(Integer.toString(j)).getData(), Integer.toString(i) + "a" + Integer.toString(j));
                Module cur2 = cur.getChilds().get(Integer.toString(j));
                if (i % 4 == 0) {
                    for (int z = 0; z < 2; ++z) {
                        assertEquals(cur2.getChilds().get(Integer.toString(z)).getName(), Integer.toString(z));
                        assertEquals(cur2.getChilds().get(Integer.toString(z)).getData(),
                                Integer.toString(i) + "a" + Integer.toString(j) + "a" + Integer.toString(z));
                    }
                }
            }
        }

        ArrayList<Commit> commits2 = new ArrayList<Commit>();
        for (int i = 0; i < 3; ++i) {
            ChangerCommit first = new ChangerCommit(k++, "_" + Integer.toString(i), "");
            commits2.add(first);
            for (int j = 0; j < 10; ++j) {
                ChangerCommit second = new ChangerCommit(k++,
                        "_" + Integer.toString(i) + "_" + Integer.toString(j)
                        , Integer.toString(i) + "a" + Integer.toString(j) + " changed");
                commits2.add(second);
                if (i % 4 == 0) {
                    for (int z = 0; z < 2; ++z) {
                        ChangerCommit commit = new ChangerCommit(k++,
                                "_" + Integer.toString(i) + "_" + Integer.toString(j) + "_" + Integer.toString(z)
                                , Integer.toString(i) + "a" + Integer.toString(j) + "a" + Integer.toString(z) + " changed");
                        commits2.add(commit);
                    }
                }
            }
        }
        model.changeSnapshot(commits2);
        assertEquals(root.getChilds().size(), 3);
        assertEquals(root.getName(), "");
        assertEquals(root.getData(), "");
        for (int i = 0; i < 3; ++i) {
            assertEquals(root.getChilds().get(Integer.toString(i)).getName(), Integer.toString(i));
            assertEquals(root.getChilds().get(Integer.toString(i)).getData(), "");
            Module cur = root.getChilds().get(Integer.toString(i));
            for (int j = 0; j < 10; ++j) {
                assertEquals(cur.getChilds().get(Integer.toString(j)).getName(), Integer.toString(j));
                assertEquals(cur.getChilds().get(Integer.toString(j)).getData(), Integer.toString(i) + "a" + Integer.toString(j)  + " changed");
                Module cur2 = cur.getChilds().get(Integer.toString(j));
                if (i % 4 == 0) {
                    for (int z = 0; z < 2; ++z) {
                        assertEquals(cur2.getChilds().get(Integer.toString(z)).getName(), Integer.toString(z));
                        assertEquals(cur2.getChilds().get(Integer.toString(z)).getData(),
                                Integer.toString(i) + "a" + Integer.toString(j) + "a" + Integer.toString(z)  + " changed");
                    }
                }
            }
        }

        ArrayList<Commit> commits3 = new ArrayList<Commit>();
        DeletionCommit commit1 = new DeletionCommit(k++, "_1");
        DeletionCommit commit2 = new DeletionCommit(k++, "_0_0_0");
        commits3.add(commit1);
        commits3.add(commit2);
        model.changeSnapshot(commits3);
        assertEquals(root.getChilds().size(), 2);
        assertEquals(root.getChilds().get("0").getChilds().get("0").getChilds().size(), 1);
    }
    @Test
    public void TestChangeSnapshotMixedUp() {
        Model model = new Model();
        Module root = model.getRoot();
        ArrayList<Commit> commits = GenerateCommitsForTest();
        model.changeSnapshot(commits);
        assertEquals(root.getChilds().size(), 4);
        for (int i = 2; i < 10; i += 2) {
            Module cur = root.getChilds().get(Integer.toString(i));
            assertEquals(cur.getData(), Integer.toString(i));
            for (int j = 0; j < 3; ++j) {
                if (j == 0) {
                    assertEquals(cur.getChilds().get(Integer.toString(j)).getData(), "_" + Integer.toString(i) + "_" +
                            Integer.toString(j) + " changed");
                } else {
                    assertEquals(cur.getChilds().get(Integer.toString(j)).getData(), Integer.toString(i) + "a" +
                            Integer.toString(j));
                }
            }
        }
    }

    @Test
    public void TestGetModuleByPath() {
        Model model = new Model();
        Module root = model.getRoot();
        ArrayList<Commit> commits = GenerateCommitsForTest();
        model.changeSnapshot(commits);
        try {
            assertEquals(model.getModule("_4_0").getFullName(), "_4_0");
            assertEquals(model.getModule("_4_0").getData(), "_4_0 changed");
            assertEquals(model.getModule("_4_1").getData(), "4a1");
            assertEquals(model.getModule("_4_2").getData(), "4a2");
            assertEquals(model.getModule("_2").getChilds().size(), 3);
            assertEquals(model.getModule("_2_0").getFullName(), "_2_0");
            assertEquals(model.getModule("_2_0").getData(), "_2_0 changed");
            assertEquals(model.getModule("_2_1").getData(), "2a1");
            assertEquals(model.getModule("_2_2").getData(), "2a2");
        } catch (Exception e) {
            assertEquals(1, 0);
        }
    }


    private ArrayList<Commit> GenerateCommitsForTest() {
        ArrayList<Commit> commits = new ArrayList<Commit>();
        int k = 0;
        for (int i = 0; i < 10; ++i) {
            if (i % 2 == 0) {
                CreationCommit commit = new CreationCommit(k++, "_" + Integer.toString(i), Integer.toString(i));
                commits.add(commit);
                for (int j = 0; j < 3; ++j) {
                    CreationCommit commit1 = new CreationCommit(k++, "_" + Integer.toString(i) + "_" + Integer.toString(j),
                            Integer.toString(i) + "a" + Integer.toString(j));
                    commits.add(commit1);
                }
                ChangerCommit ch_commit = new ChangerCommit(k++, "_" + Integer.toString(i) + "_" + Integer.toString(0),
                        "_" + Integer.toString(i) + "_" + Integer.toString(0) + " changed");
                commits.add(ch_commit);
            }
        }
        DeletionCommit commit = new DeletionCommit(k++, "_" + Integer.toString(0));
        commits.add(commit);
        return commits;
    }
}
