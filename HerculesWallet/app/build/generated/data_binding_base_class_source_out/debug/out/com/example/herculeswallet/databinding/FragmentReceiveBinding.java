// Generated by view binder compiler. Do not edit!
package com.example.herculeswallet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.herculeswallet.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentReceiveBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView addressCrypto;

  @NonNull
  public final ImageView addressQr;

  @NonNull
  public final ImageView iconCrypto;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final RelativeLayout intestazione;

  @NonNull
  public final LinearLayout linear1;

  @NonNull
  public final ImageView logoDashboard;

  @NonNull
  public final TextView textView;

  private FragmentReceiveBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView addressCrypto, @NonNull ImageView addressQr, @NonNull ImageView iconCrypto,
      @NonNull ImageView imageView, @NonNull RelativeLayout intestazione,
      @NonNull LinearLayout linear1, @NonNull ImageView logoDashboard, @NonNull TextView textView) {
    this.rootView = rootView;
    this.addressCrypto = addressCrypto;
    this.addressQr = addressQr;
    this.iconCrypto = iconCrypto;
    this.imageView = imageView;
    this.intestazione = intestazione;
    this.linear1 = linear1;
    this.logoDashboard = logoDashboard;
    this.textView = textView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentReceiveBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentReceiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_receive, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentReceiveBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.address_crypto;
      TextView addressCrypto = ViewBindings.findChildViewById(rootView, id);
      if (addressCrypto == null) {
        break missingId;
      }

      id = R.id.address_qr;
      ImageView addressQr = ViewBindings.findChildViewById(rootView, id);
      if (addressQr == null) {
        break missingId;
      }

      id = R.id.icon_crypto;
      ImageView iconCrypto = ViewBindings.findChildViewById(rootView, id);
      if (iconCrypto == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.intestazione;
      RelativeLayout intestazione = ViewBindings.findChildViewById(rootView, id);
      if (intestazione == null) {
        break missingId;
      }

      id = R.id.linear_1;
      LinearLayout linear1 = ViewBindings.findChildViewById(rootView, id);
      if (linear1 == null) {
        break missingId;
      }

      id = R.id.logo_dashboard;
      ImageView logoDashboard = ViewBindings.findChildViewById(rootView, id);
      if (logoDashboard == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      return new FragmentReceiveBinding((ConstraintLayout) rootView, addressCrypto, addressQr,
          iconCrypto, imageView, intestazione, linear1, logoDashboard, textView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
