// CENG-322-0NC Francisco Santos n01423860, Pradeep Singh n00975892
// CENG-322-0NB Ralph Robes n01410324, Elijah Tanimowo n01433560
package ca.nika.it.gear5;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public interface FragmentNavi {
    void naviFrag(Fragment fragment, boolean addToStack);
}
