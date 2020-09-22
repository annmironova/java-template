package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntSort {
  public static void sort(int array[]) {
    int ord = 1, jump = 0, size2, length = array.length;
    int[] newArray, arr1, arr2;

    while (ord < length) {
      while (jump < length && jump + ord < length) {
        size2 = (jump + ord * 2 > length) ? length - jump - ord : ord;

        arr1 = Arrays.copyOfRange(array, jump, jump + ord);
        arr2 = Arrays.copyOfRange(array, jump + ord, jump + ord + size2);
        newArray = merge(arr1, arr2);

        for (int i = 0; i < size2 + ord; i++)
          array[jump + i] = newArray[i];
        jump += 2 * ord;
      }

      jump = 0;
      ord *= 2;
    }
  }

  public static int[] merge(int[] arr1, int[] arr2) {
    int length1, length2, length;
    length1 = arr1.length;
    length2 = arr2.length;
    length = length1 + length2;
    int k = 0, l = 0;
    int[] result = new int[length];

    for (int i = 0; i < length; i++) {
      if (l < length2 && k < length1) {
        if (arr1[k] > arr2[l]) {
          result[i] = arr2[l];
          l++;
        }
        else {
          result[i] = arr1[k];
          k++;
        }
      } else if (l < length2) {
        result[i] = arr2[l];
        l++;
      } else {
        result[i] = arr1[k];
        k++;
      }
    }
    return result;
  }

  public static void sort(List<Integer> list) {
    Collections.sort(list);
  }
}