package com.nateshoffner.seachemdoser.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.ui.view.DosageResultView;
import com.nateshoffner.seachemdoser.utils.StringUtils;

public class DosageDialog extends MaterialDialog {

    private boolean mViewingNotes;
    private boolean mViewingWarnings;
    private SeachemDosage[] mDosages;
    private String[] mWarnings;
    private String[] mNotes;

    private MDButton btnOk = null;
    private MDButton btnWarnings = null;
    private MDButton btnNotes = null;
    private TextView notesView = null;
    private TextView warningsView = null;
    private LinearLayout dosageContainer = null;

    public DosageDialog(Builder builder, SeachemDosage[] dosages, final String[] warnings,
                        String[] notes) {
        super(builder);
        mDosages = dosages;
        mWarnings = warnings;
        mNotes = notes;
    }

    private void updateContents(MaterialDialog dialog) {
        if (isViewingWarnings()) {
            dialog.setTitle(R.string.warnings);
            dosageContainer.setVisibility(View.GONE);

            // hide notes controls
            if (notesView != null)
                notesView.setVisibility(View.GONE);
            if (btnNotes != null) {
                btnNotes.setText(R.string.notes);
                btnNotes.setVisibility(View.GONE);
            }

            warningsView.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);

            btnWarnings.setText(R.string.back_to_dosages);
        }

        else if (isViewingNotes()) {
            dialog.setTitle(R.string.notes);
            dosageContainer.setVisibility(View.GONE);

            // hide warnings controls
            if (warningsView != null)
                warningsView.setVisibility(View.GONE);
            if (btnWarnings != null) {
                btnWarnings.setText(R.string.warnings);
                btnWarnings.setVisibility(View.GONE);
            }

            notesView.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);

            btnNotes.setText(R.string.back_to_dosages);
        }

        else {
            dialog.setTitle(R.string.label_requirements);
            dosageContainer.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);

            if (notesView != null)
                notesView.setVisibility(View.GONE);
            if (btnNotes != null) {
                btnNotes.setText(R.string.notes);
                btnNotes.setVisibility(View.VISIBLE);
            }

            if (warningsView != null)
                warningsView.setVisibility(View.GONE);
            if (btnWarnings != null) {
                btnWarnings.setText(R.string.warnings);
                btnWarnings.setVisibility(View.VISIBLE);
            }
        }
    }

    public MaterialDialog getDialog(final Context context) {
        setViewingNotes(false);
        setViewingWarnings(false);

        MaterialDialog.Builder builder = super.getBuilder()
                .title(R.string.label_requirements)
                .customView(R.layout.dosage_dialog, true)
                .positiveText(R.string.changelog_ok_button)
                .cancelable(false);

        if (mWarnings.length > 0) {
            builder = builder.neutralText(R.string.warnings);
        }

        if (mNotes.length > 0) {
            builder = builder.negativeText(R.string.notes);
        }

        final MaterialDialog dialog = builder.build();

        dosageContainer = (LinearLayout)dialog.findViewById(R.id.dosages_container);

        btnOk = dialog.getActionButton(DialogAction.POSITIVE);

        if (mWarnings.length > 0) {
            btnWarnings = dialog.getActionButton(DialogAction.NEUTRAL);
            warningsView = (TextView) dialog.findViewById(R.id.dosage_warnings);
            warningsView.setText(StringUtils.join(mWarnings, "\n\n"));
        }

        if (mNotes.length > 0) {
            btnNotes = dialog.getActionButton(DialogAction.NEGATIVE);
            notesView = (TextView)dialog.findViewById(R.id.dosage_info);
            notesView.setText(StringUtils.join(mNotes, "\n\n"));
        }

        for (int i = 0, dosagesLength = mDosages.length; i < dosagesLength; i++) {
            SeachemDosage dosage = mDosages[i];
            DosageResultView view = new DosageResultView(context, null);
            view.setUnitQualifier(dosage.getUnit());
            view.setValue(dosage.getAmount());
            view.setLabelText(i < dosagesLength - 1 ? context.getString(R.string.label_dosage_or) : "");

            dosageContainer.addView(view);
        }

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface d) {

                if (mWarnings.length > 0) {
                    btnWarnings.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            setViewingWarnings(!isViewingWarnings());
                            updateContents(dialog);
                        }
                    });
                }

                if (mNotes.length > 0) {
                    btnNotes.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            setViewingNotes(!isViewingNotes());
                            updateContents(dialog);
                        }
                    });
                }

                btnOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    private boolean isViewingWarnings() {
        return mViewingWarnings;
    }

    private void setViewingWarnings(boolean viewingWarnings) {
        this.mViewingWarnings = viewingWarnings;
    }

    private boolean isViewingNotes() {
        return mViewingNotes;
    }

    private void setViewingNotes(boolean viewingNotes) {
        this.mViewingNotes = viewingNotes;
    }
}
