/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.core.ui.data.script.script_tree_list;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import ryey.easer.R;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import tellh.com.recyclertreeview_lib.TreeViewBinder;

public class TreeViewAdapterWithContextMenu extends TreeViewAdapter {
onLongItemClickListener mOnLongItemClickListener;

private final WeakReference<Context> refContext;

TreeViewAdapterWithContextMenu(final List<TreeNode> nodes,
                               final Context context) {
	this(nodes, new EventNodeBinder(), context);
}

private TreeViewAdapterWithContextMenu(final List<TreeNode> nodes,
                                       final EventNodeBinder binder,
                                       final Context context) {
	super(nodes, Arrays.asList(binder));
	binder.setRef(this);
	this.refContext = new WeakReference<>(context);
}

public void setOnLongItemClickListener(
	final onLongItemClickListener onLongItemClickListener) {
	mOnLongItemClickListener = onLongItemClickListener;
}

public interface onLongItemClickListener {
void ItemLongClicked(View v, EventItem eventItem);
}

private static class EventNodeBinder extends TreeViewBinder<ViewHolder> {

WeakReference<TreeViewAdapterWithContextMenu> ref;

public void setRef(final TreeViewAdapterWithContextMenu ref) {
	this.ref = new WeakReference<>(ref);
}

@Override
public TreeViewAdapterWithContextMenu.ViewHolder
provideViewHolder(final View view) {
	return new TreeViewAdapterWithContextMenu.ViewHolder(view);
}

@Override
public void
bindView(final TreeViewAdapterWithContextMenu.ViewHolder viewHolder,
         final int position, final TreeNode treeNode) {
	final EventItem item = (EventItem)treeNode.getContent();

	viewHolder.itemView.setOnLongClickListener(
		new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(final View v) {
				        if (ref.get().mOnLongItemClickListener != null) {
				                ref.get().mOnLongItemClickListener.ItemLongClicked(v, item);
					}

				        return true;
				}
			});

	viewHolder.tvEventName.setText(item.eventName);

	@ColorRes final int textColor;
	if (!item.valid)
		textColor = R.color.colorText_invalid;
	else if (!item.active)
		textColor = R.color.colorText_scriptInactive;
	else
		textColor = R.color.colorText;
	viewHolder.tvEventName.setTextColor(
		ContextCompat.getColor(ref.get().refContext.get(), textColor));

	viewHolder.ivArrow.setImageResource(
		R.drawable.ic_keyboard_arrow_right_black_24dp);
	int rotateDegree = treeNode.isExpand() ? 90 : 0;
	viewHolder.ivArrow.setRotation(rotateDegree);
	viewHolder.tvEventName.setText(item.eventName);
	if (treeNode.isLeaf())
		viewHolder.ivArrow.setVisibility(View.INVISIBLE);
	else
		viewHolder.ivArrow.setVisibility(View.VISIBLE);
}

@Override
public int getLayoutId() {
	return R.layout.item_script_data_node;
}
}

static final class ViewHolder extends TreeViewBinder.ViewHolder {

final TextView tvEventName;
final ImageView ivArrow;

ViewHolder(final View rootView) {
	super(rootView);
	tvEventName = rootView.findViewById(R.id.tv_name);
	ivArrow = rootView.findViewById(R.id.iv_arrow);
}
}
}
