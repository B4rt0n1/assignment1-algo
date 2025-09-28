## 1. MergeSort

**Algorithm:** Classic divide-and-conquer mergesort with linear merge, reusable buffer, and a small-n insertion sort cutoff.

**Recurrence:**

T(n) = 2T(n/2) + O(n)

a = 2 (two subproblems)
b = 2 (subproblem size is n/2)
f(n) = O(n)

**Master Theorem (Case 2):**

n^(log_b a) = n^(log_2 2) = n

Since f(n) = Θ(n), this is Case 2 of the Master Theorem:

T(n) = Θ(n log n)

**Empirical:**
Plots of runtime vs. n confirm `O(n log n)` growth. Recursion depth grows as `log n`. The insertion sort cutoff improves constant factors for small n but does not change asymptotic complexity.


## 2. QuickSort

**Algorithm:** Randomized pivot selection, smaller-first recursion, iterative handling of the larger partition.

**Recurrence (average case):**

T(n) = 2T(n/2) + O(n)

This gives:

T(n) = Θ(n log n)

**Recurrence (worst case):**

T(n) = T(n - 1) + O(n) = O(n^2)

However, with randomization, worst-case behavior is extremely unlikely. Smaller-first recursion ensures recursion depth remains `O(log n)`.

Measured runtime closely follows `n log n` and recursion depth is typically close to `2 log₂ n`. Random pivoting eliminates pathological input cases.


## 3. Deterministic Select (Median-of-Medians)

**Algorithm:** Selects the median of medians as pivot in linear time, recursing into one subproblem only.

**Recurrence:**

T(n) = T(n/5) + T(7n/10) + O(n)

**Akra–Bazzi intuition:**
The dominant term is from the larger partition (7n/10):

T(n) ≈ T(0.7n) + O(n)

By applying Akra–Bazzi or repeated substitution:

T(n) = Θ(n)

**Empirical:**
Runtime grows linearly and is consistently faster than sorting followed by element selection when only one order statistic is needed. Overhead from pivot selection makes it slower for small n.


## 4. Closest Pair of Points (2D)

**Algorithm:** Sort points by x-coordinate, divide by median x, recursively solve subproblems, and check the “strip” for cross-boundary pairs.

**Recurrence:**

T(n) = 2T(n/2) + O(n)

a = 2, b = 2, f(n) = O(n)

By Master Theorem (Case 2):
T(n) = Θ(n log n)

**Empirical:**
Runtime follows `n log n` with noticeable constant overhead from initial sorting and strip checking. Recursion depth remains `O(log n)`.

## 5. Recursion Depth and Space


 MergeSort             O(log n)                 
 QuickSort             O(log n) (smaller-first) 
 Deterministic Select  O(log n)                 
 Closest Pair          O(log n)                 

Empirical measurements confirm recursion depth ≈ `2 log₂ n`, validating the safe recursion strategies.


## 6. Empirical Plots

Collected data with the CLI runner and CSV logger for each algorithm, measuring:

 Execution time (ms)
 Number of comparisons
 Number of allocations
 Maximum recursion depth

Example scaling trends (n = 1k–100k):

 Algorithm             Theoretical  Observed Growth 

 MergeSort             O(n log n)   ~ c₁ n log n   
 QuickSort (avg)       O(n log n)   ~ c₂ n log n    
 Deterministic Select  O(n)         ~ c₃ n          
 Closest Pair          O(n log n)   ~ c₄ n log n    

Empirical results match theoretical expectations. Minor deviations at small n are due to cache effects, JIT warm-up, and insertion sort cutoffs.

## 7. Summary

* All four algorithms exhibit their expected asymptotic complexities.
* Master Theorem and Akra–Bazzi analyses correctly predict the observed runtime behavior.
* Recursion depth measurements align with `O(log n)`.
* Constant factors differ: QuickSort often outperforms MergeSort due to better cache locality.
* Deterministic Select is faster than full sorting for large n when only a single order statistic is required.