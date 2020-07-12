/**
 * Copyright 2013 Yamato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mod.ymt.cmn;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * ��Q�Ƃɂėv�f��ێ����� Collection �ł��B
 * @author Yamato
 */
public class WeakCollection<E> extends AbstractCollection<E> implements Collection<E> {
	protected static final Logger logger = Logger.getLogger(WeakCollection.class.getName());
	protected final List<Reference<E>> inner = new ArrayList<Reference<E>>();

	/**
	 * value ����Q�Ƃɂ� WeakCollection �ɒǉ����܂��B
	 */
	@Override
	public boolean add(E value) {
		if (value == null)
			throw new NullPointerException("WeakCollection value cant be null");
		return inner.add(new WeakReference<E>(value));
	}

	/**
	 * WeakCollection �̗v�f�Ɋ֌W���锽���q��Ԃ��܂��B
	 * ���� Iterator �� next �͏��������Q�Ƃɑ΂��� null ��Ԃ��܂��B
	 * ���̍ہA�����I�ɗv�f�� remove ����܂��B
	 */
	@Override
	public Iterator<E> iterator() {
		final Iterator<Reference<E>> iter = inner.iterator();
		return new Iterator<E>() {
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public E next() {
				Reference<E> ref = iter.next();
				E result = ref.get();
				if (result == null) {
					iter.remove();
					logger.fine("delete Reference");
				}
				return result;
			}

			@Override
			public void remove() {
				iter.remove();
			}
		};
	}

	/**
	 * ���݂̃R���N�V�����T�C�Y��Ԃ��܂��B
	 * ���̒l�ɂ͂��łɏ��������Q�Ƃ��܂ނ��߁A���ۂ̃I�u�W�F�N�g���͂����菭�Ȃ��\��������܂��B
	 */
	@Override
	public int size() {
		return inner.size();
	}
}
